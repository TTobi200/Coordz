package de.gui.comp;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.util.*;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;

import de.coordz.CooSystem;
import de.coordz.data.CooCustomer;
import de.coordz.data.base.CooImage;
import de.coordz.db.CooDBSelectStmt;
import de.coordz.db.gen.inf.InfImage;
import de.gui.*;
import de.util.*;
import de.util.log.CooLog;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CooImageGallery extends BorderPane
{
	/** The default progress label padding */
	final private static int DEFAULT_LABEL_PADDING = 20;
	
	/** The {@link TilePane} with loaded images */
	private TilePane tilePane;
	/** The loading progress {@link Label} */
	private Label lblProgress;
	/** The loading progress {@link ProgressBar} */
	private ProgressBar progress;

	public CooImageGallery()
	{
		// Surrounding scroll pane
		ScrollPane scrollPane = new ScrollPane();
		tilePane = new TilePane();
		tilePane.setPadding(new Insets(10, 10, 10, 10));
		tilePane.setHgap(10);

		// Stackpane for progressbar with progress label
		StackPane stack = new StackPane();
		lblProgress = new Label();
		progress = new ProgressBar();
		progress.setDisable(Boolean.TRUE);
		progress.setMaxWidth(Double.MAX_VALUE);
		progress.setMinHeight(lblProgress.getBoundsInLocal()
			.getHeight() + DEFAULT_LABEL_PADDING);
		
		stack.getChildren().addAll(progress, lblProgress);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); 
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setFitToWidth(Boolean.TRUE);
		scrollPane.setContent(tilePane);
		
		// Add the view in center and loading to bottom
		setCenter(scrollPane);
		setBottom(stack);
		
		// Accept the drag events
		setOnDragOver(e ->
		{
			Dragboard db = e.getDragboard();
			e.acceptTransferModes(db.hasFiles() ?
				TransferMode.COPY : null);
			e.consume();
		});	
	}
	
	public void loadImagesDB(CooCustomer customer, boolean load)
	{
		// Run the image loading in separated thread
		Task<Void> task = new Task<Void>()
		{
			@Override
			protected Void call() throws Exception
			{
				// Set the loading cursor
				setCursor(Cursor.WAIT);
				
				// First clear the view
				Platform.runLater(() -> tilePane
					.getChildren().clear());
				
				// Check if we should load and images are available
				if(load && Objects.nonNull(customer))
				{
					// Create statement to load customer images
					CooDBSelectStmt stmt = new CooDBSelectStmt();
					stmt.addFrom(InfImage.TABLE_NAME);
					stmt.addColumn("*");
					stmt.addWhere(InfImage.CUSTOMERID + " = ?", 
						customer.customerIdProperty().get());

					ResultSet res = CooSystem.getDatabase().execQuery(stmt);
					int i = 0;

					// Check if we have files to display
					if(!res.next())
					{
						updateProgress(1, 1);
						updateMessage("Keine Bilder vorhanden");
					}
					else
					{
						do
						{
							CooImage daoImage = new CooImage();
							daoImage.cre(res);
							
							// Create a new image slide
							VBox imageSlide = createImageSlideDB(
								daoImage.nameProperty().get(),
								daoImage.load(150, 0));
							
							// Add the image later for loading effect
							Platform.runLater(() -> 
								tilePane.getChildren().add(imageSlide));

							// Update the loading progress
							// TODO $TO: Fix the loading progress 
							updateProgress(++i, i);
							updateMessage(i + " / " + i);
						}while(res.next());
					}
				}
				
				// Set the default cursor
				setCursor(Cursor.DEFAULT);
				return null;
			}
		};
		// Bind the task progress and text to progress bar
		lblProgress.textProperty().bind(task.messageProperty());
		progress.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
		
		// Forward the dragged file to copy it to image folder
//		setOnDragDropped(e -> copyImage(imgFolder, e));
	}
	
	public void loadImages(File imgFolder, boolean load)
	{
		// Run the image loading in separated thread
		Task<Void> task = new Task<Void>()
		{
			@Override
			protected Void call() throws Exception
			{
				// Set the loading cursor
				setCursor(Cursor.WAIT);
				
				// First clear the view
				Platform.runLater(() -> tilePane
					.getChildren().clear());
				
				// Check if we should load and folder is available
				if(load && Objects.nonNull(imgFolder) && 
					// Check if folder exists or if we can create it
					imgFolder.exists() || imgFolder.mkdir())
				{
					// List all files and loop through them
					File[] listOfFiles = imgFolder.listFiles();
					
					// Check if we have files to load
					if(listOfFiles.length <= 0)
					{
						updateProgress(1, 1);
						updateMessage("Keine Bilder vorhanden");
					}
					
					for(int i = 0; i < listOfFiles.length; i++)
					{
						// Create a new image slide
						VBox imageSlide = createImageSlide(imgFolder, listOfFiles[i]);
						
						// Add the image later for loading effect
						Platform.runLater(() -> 
							tilePane.getChildren().add(imageSlide));

						// Update the loading progress
						updateProgress(i + 1, listOfFiles.length);
						updateMessage((i + 1) + " / " + listOfFiles.length);
					}
				}
				
				// Set the default cursor
				setCursor(Cursor.DEFAULT);
				return null;
			}
		};
		// Bind the task progress and text to progress bar
		lblProgress.textProperty().bind(task.messageProperty());
		progress.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
		
		// Forward the dragged file to copy it to image folder
		setOnDragDropped(e -> copyImage(imgFolder, e));
	}
	
	public void copyImage(File imgFolder, DragEvent e)
	{
		Dragboard db = e.getDragboard();
		List<File> files = db.getFiles();
		
		// Run the image loading in separated thread
		Task<Void> task = new Task<Void>()
		{
			@Override
			protected Void call() throws Exception
			{
				// Set the loading cursor
				setCursor(Cursor.WAIT);
				
				// Check if we have a file dragged
				if(!files.isEmpty())
				{
					for(File file : files)
					{
						try(ImageInputStream iis = ImageIO.createImageInputStream(file))
						{
							// Load the image file and find a image writer
							BufferedImage image = ImageIO.read(file.toURI().toURL());
							Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
							
							// TODO $TO: Display a message when file already exists?
							if(imageReaders.hasNext() && Objects.nonNull(file))
							{
								// Write the image into image folder
								ImageReader reader = imageReaders.next();
								ImageIO.write(image, reader.getFormatName(),
									new File(imgFolder, file.getName()));
							}		
						}
						catch(IOException ex)
						{
							CooLog.error("Error while copying image", ex);
						}
					}
				}
				
				// Start reloading all images
				loadImages(imgFolder, Boolean.TRUE);
				
				// Complete the drag event
				e.setDropCompleted(Boolean.TRUE);
				e.consume();
				
				// Set the default cursor
				setCursor(Cursor.DEFAULT);
				return null;
			}
		};
		// Bind the task progress and text to progress bar
		lblProgress.textProperty().bind(task.messageProperty());
		progress.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
	}
	
	private VBox createImageSlide(File imageFolder, File imageFile)
	{
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		
		try(FileInputStream stream = new FileInputStream(imageFile))
		{
			// Load the image to image view
			Image image = new Image(stream, 150,
				0, Boolean.TRUE,	Boolean.TRUE);
			ImageView imageView = new ImageView(image);
			imageView.setEffect(new DropShadow(15,
				Color.DARKGRAY));
			imageView.setFitWidth(150);
			imageView.setPickOnBounds(Boolean.TRUE);
			
			// Load the delete image icon
			Image imgDelete = CooFileUtil.getResourceIcon("delete_cross.png");
			ImageView imgViewDelete = new ImageView(imgDelete);
			imgViewDelete.setEffect(new DropShadow(10,
				Color.DARKGRAY));
			imgViewDelete.setTranslateX(
				// Move to right site
				(imgViewDelete.getTranslateX()
				+ imageView.getBoundsInParent().getWidth() / 2)
				// Move backwards image size and offset
				- imgDelete.getWidth() - 15);
			imgViewDelete.setTranslateY(
				// Move to the top
				(imgViewDelete.getTranslateY() 
				- imageView.getBoundsInParent().getHeight() / 2)
				// Move down image size and offset
				+ imgDelete.getWidth() + 15);
			imgViewDelete.setVisible(Boolean.FALSE);
			
			// Display the delete icon on mouse over
			imgViewDelete.setOnMouseEntered(e -> imgViewDelete.setVisible(Boolean.TRUE));
			imageView.setOnMouseEntered(e -> imgViewDelete.setVisible(Boolean.TRUE));
			imageView.setOnMouseExited(e -> imgViewDelete.setVisible(Boolean.FALSE));
			
			// Add action to delete image
			imgViewDelete.setPickOnBounds(Boolean.TRUE);
			imgViewDelete.setOnMouseClicked(e -> 
				imgViewDelete.setVisible(deleteImage(
					imageFolder, imageFile)));
			
			imageView.setOnMouseClicked(e -> 
			{
				// Check if double left click
				if(e.getButton().equals(MouseButton
					.PRIMARY) && e.getClickCount() == 2)
				{
					openImageDetail(imageFile);
				}
			});
			
			// Image slide vbox with image and its name
			vbox.getChildren().add(new StackPane(imageView, imgViewDelete));
			vbox.getChildren().add(new Label(imageFile.getName()));
		}
		catch(IOException ex)
		{
			CooLog.error("Error while "
				+ "loading image", ex);
		}
		return vbox;
	}
	
	private VBox createImageSlideDB(String name, Image image)
	{
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		
		// Load the image to image view
//		Image image = new Image(stream, 150,
//			0, Boolean.TRUE,	Boolean.TRUE);
			
		ImageView imageView = new ImageView(image);
		imageView.setEffect(new DropShadow(15,
			Color.DARKGRAY));
		imageView.setFitWidth(150);
		imageView.setPickOnBounds(Boolean.TRUE);
		
		// Load the delete image icon
		Image imgDelete = CooFileUtil.getResourceIcon("delete_cross.png");
		ImageView imgViewDelete = new ImageView(imgDelete);
		imgViewDelete.setEffect(new DropShadow(10,
			Color.DARKGRAY));
		imgViewDelete.setTranslateX(
			// Move to right site
			(imgViewDelete.getTranslateX()
			+ imageView.getBoundsInParent().getWidth() / 2)
			// Move backwards image size and offset
			- imgDelete.getWidth() - 15);
		imgViewDelete.setTranslateY(
			// Move to the top
			(imgViewDelete.getTranslateY() 
			- imageView.getBoundsInParent().getHeight() / 2)
			// Move down image size and offset
			+ imgDelete.getWidth() + 15);
		imgViewDelete.setVisible(Boolean.FALSE);
		
		// Display the delete icon on mouse over
		imgViewDelete.setOnMouseEntered(e -> imgViewDelete.setVisible(Boolean.TRUE));
		imageView.setOnMouseEntered(e -> imgViewDelete.setVisible(Boolean.TRUE));
		imageView.setOnMouseExited(e -> imgViewDelete.setVisible(Boolean.FALSE));
		
		// Add action to delete image
		imgViewDelete.setPickOnBounds(Boolean.TRUE);
//		imgViewDelete.setOnMouseClicked(e -> 
//			imgViewDelete.setVisible(deleteImage(
//				imageFolder, imageFile)));
		
		imageView.setOnMouseClicked(e -> 
		{
			// Check if double left click
			if(e.getButton().equals(MouseButton
				.PRIMARY) && e.getClickCount() == 2)
			{
//				openImageDetail(imageFile);
			}
		});
		
		// Image slide vbox with image and its name
		vbox.getChildren().add(new StackPane(imageView, imgViewDelete));
		vbox.getChildren().add(new Label(name));
			
		return vbox;
	}

	private boolean deleteImage(File imageFolder, File imageFile)
	{
		// Return if image has been deleted
		boolean deleted = Boolean.FALSE;
		
		// Ask the user if really wants to delete image
		if(Objects.nonNull(imageFile) && CooDialogs.showConfirmDialog(
			getScene().getWindow(),	"Bild löschen", "Wollen Sie das Bild "
			+ imageFile.getName() + " wirklich löschen?"))
		{
			try
			{
				// Delete the image file and remove the slide
				Files.delete(imageFile.toPath());
				loadImages(imageFolder, Boolean.TRUE);
				deleted = Boolean.TRUE;
			}
			catch(IOException e)
			{
				CooLog.error("Error while deleting image", e);
			}
		}
		
		return deleted;
	}

	private void openImageDetail(File imageFile)
	{
		try(FileInputStream stream = new FileInputStream(imageFile))
		{
			Stage newStage = new Stage();
			BorderPane borderPane = new BorderPane();
			Scene scene = new Scene(borderPane);
			ImageView imageView = new ImageView();
			Image imageLarge = new Image(stream);
			
			// Load the image cached ad smooth
			imageView.setImage(imageLarge);
			imageView.setPreserveRatio(Boolean.TRUE);
			imageView.setSmooth(Boolean.TRUE);
			imageView.setCache(Boolean.TRUE);
			borderPane.setCenter(imageView);
			
			// Create the new stage 
			newStage.setWidth(750);
			newStage.setHeight(500);
			newStage.setTitle(CooMainFrame.TITLE 
				+ " - " + imageFile.getName());
			newStage.getIcons().add(CooFileUtil
				.getResourceIcon("Logo.png"));
			CooGuiUtil.relativeToOwner(newStage, 
				getScene().getWindow());

			// Use max size for image view in scene
			imageView.fitWidthProperty().bind(newStage.widthProperty());
			imageView.fitHeightProperty().bind(newStage.heightProperty());
			
			newStage.setScene(scene);
			newStage.show();
		}
		catch(IOException ex)
		{
			CooLog.error("Error while opening"
				+ " image in window", ex);
		}		
	}
}