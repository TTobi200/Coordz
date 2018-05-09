/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.doc;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.*;

import de.coordz.data.*;
import de.coordz.data.base.*;
import de.util.CooFileUtil;
import de.util.log.CooLog;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CooXlsDocument extends CooDocument
{
	public static enum Columns
	{
		A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;

		public static Columns parse(int ordinal)
		{
			for(Columns c : Columns.values())
			{
				if(c.ordinal() == ordinal)
				{
					return c;
				}
			}
			return null;
		}
	}
	
	@Override
	public void save(File file, CooCustomer customer, CooProject... projects)
	{
		try
		{
			for(CooProject project : projects)
			{
				for(CooStation station : project.getStations())
				{
					// Create <Projectname>_<GivenFileName>_<Stationname>.xls
					File xlsxFile = new File(file.getParentFile(),
						project.nameProperty().get() + "_" + file.getName()
						+ "_" + station.nameProperty().get() + ".xls");
					
					HSSFWorkbook wb = new HSSFWorkbook();
					if(getContent().contains(Content.TITLE_PAGE))
					{
						// Add a title page
						addTitlePage(wb, customer, station);
					}
					if(getContent().contains(Content.MEASUREMENTS))
					{
						// Add all measurements
						station.getMeasurements().forEach(m ->
							addMeasurement(wb, station, m));
					}
					if(getContent().contains(Content.VERIFY_MEASUREMENT))
					{
						// And add the control measurement
						addVerifyMeasurement(wb, station);
					}
					
					FileOutputStream fOut = new FileOutputStream(
						xlsxFile.getAbsolutePath());
					wb.write(fOut);
					fOut.close();
				}
			}
		}
		catch(IOException e)
		{
			CooLog.error("Error while saving Excel file", e);
		}
	}

	protected void addTitlePage(HSSFWorkbook wb, CooCustomer customer, CooStation station)
	{
		HSSFSheet sheet = wb.createSheet(WorkbookUtil.createSafeSheetName(
			"Titelseite"));
		
		// add the header
		if(getContent().contains(Content.HEADER_FOOTER))
		{
			addHeader(wb, sheet, station);
		}
		
		// Add the customer logo
		addMergedBorderedCell(wb, sheet, 3, 23, Columns.C, Columns.J, 
			"",
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		addPicture(wb, sheet, 3, 23, Columns.C, Columns.J, 
			customer.logoProprty().get());
		
		// Add the customer adress
		addMergedBorderedCell(wb, sheet, 25, 25, Columns.C, Columns.J, 
			customer.nameProperty().get(),
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE,
			true);
		addMergedBorderedCell(wb, sheet, 26, 26, Columns.C, Columns.J, 
			customer.streetProperty().get(),
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			true);
		addMergedBorderedCell(wb, sheet, 27, 27, Columns.C, Columns.J, 
			customer.plzProperty().get() + " " + customer.locationProperty().get(),
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			true);
	}

	protected void addVerifyMeasurement(HSSFWorkbook wb, CooStation station)
	{
		HSSFSheet sheet = wb.createSheet(WorkbookUtil.createSafeSheetName(
			"Referenzmessung"));
		CooVerifyMeasurement verify = station.verifyMeasurementProperty().get();

		// add the header
		if(getContent().contains(Content.HEADER_FOOTER))
		{
			addHeader(wb, sheet, station);
		}
		
		// Add the given values
		if(getContent().contains(Content.SPECIFICATION))
		{
			addSpecification(wb, sheet, verify);
		}
		
		// Add the measured values
		if(getContent().contains(Content.RESULT))
		{
			addResult(wb, sheet, verify);
		}
	}

	protected void addMeasurement(HSSFWorkbook wb, CooStation station, CooMeasurement measurement)
	{
		HSSFSheet sheet = wb.createSheet(WorkbookUtil.createSafeSheetName(
			measurement.nameProperty().get()));

		// Add the header
		if(getContent().contains(Content.HEADER_FOOTER))
		{
			addHeader(wb, sheet, station);
		}
		
		// Add the columnHeader
		addColumnHeader(wb, sheet);
		
		// Add the reticles
		if(getContent().contains(Content.RETICLES))
		{
			addReticles(wb, sheet, measurement);
		}
		
		// Add the position
		addPosition(wb, sheet);
		
		// Add the laser deviation
		addLaserDeviation(wb, sheet);
		
		// Add the targets
		if(getContent().contains(Content.TARGETS))
		{
			addTargets(wb, sheet, measurement);
		}
		
		// Add the targets to laser
		addTargetsToLaser(wb, sheet);
		
		// Add the projection height
		addProjectionHeight(wb, sheet);
		
		// Add the lux 
		addLux(wb, sheet);
		
		// Add the date/time/weather
		addDateTimeWeather(wb, sheet, measurement);
	}
	
	protected void addResult(HSSFWorkbook wb, HSSFSheet sheet, CooVerifyMeasurement verify)
	{
		addMergedBorderedCell(wb, sheet, 8, 8, Columns.A, Columns.L,
			"Gemessene Werte",
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE,
			true);
		addMergedBorderedCell(wb, sheet, 9, 9, Columns.E, Columns.G,
			"Grundmaße",
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			true);
		addMergedBorderedCell(wb, sheet, 9, 9, Columns.H, Columns.J,
			"Position (Paletten 0 Punkt)", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			true);
		addMergedBorderedCell(wb, sheet, 9, 9, Columns.K, Columns.L,
			"Diagonalen", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			true);
		// Ground sizes
		addBorderedCell(wb, sheet, 10, Columns.E, 
			"Länge", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 10, Columns.F, 
			"Breite", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 10, Columns.G, 
			"Höhe", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		// Position 
		addBorderedCell(wb, sheet, 10, Columns.H, 
			"X", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 10, Columns.I, 
			"Y", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 10, Columns.J, 
			"Z", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		// Diagonals
		addBorderedCell(wb, sheet, 10, Columns.K, 
			"D1",
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 10, Columns.L, 
			"D2", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		
		addMergedBorderedCell(wb, sheet, 11, 11, Columns.A, Columns.B,
			"Geometriedarstellung", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		addMergedBorderedCell(wb, sheet, 11, 11, Columns.C, Columns.D,
			verify.getResult().stream().findFirst().get().nameProperty().get(), 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		
		verify.getResult().stream().findFirst().ifPresent(res ->
		{
			addDataRow(wb, sheet, Columns.E, 11, 
				res.lengthProperty().get(),
				res.widthProperty().get(),
				res.heightProperty().get());
			addDataRow(wb, sheet, Columns.H, 11,
				res.xProperty().get(),
				res.yProperty().get(), 
				res.zProperty().get());
			addDataRow(wb, sheet, Columns.K, 11, 
				res.d1Property().get(),
				res.d2Property().get(),
				Double.NaN);
		});
		
		addMergedBorderedCell(wb, sheet, 12, 12, Columns.C, Columns.D,
			"Messpunkte", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		addDataRow(wb, sheet, Columns.E, 12, 1, 2, Double.NaN);
		addDataRow(wb, sheet, Columns.H, 12, 1, 3, Double.NaN);
		
		// Add the point graphic
		addResPicture(wb, sheet, 13, 33, Columns.A, Columns.L, "Beispiel_Referenzmessung.png");
	}
	
	protected void addSpecification(HSSFWorkbook wb, HSSFSheet sheet, CooVerifyMeasurement verify)
	{
		addMergedBorderedCell(wb, sheet, 3, 3, Columns.A, Columns.L,
			"Vorgegebene Werte", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE,
			true);
		addMergedBorderedCell(wb, sheet, 4, 4, Columns.E, Columns.G,
			"Grundmaße", 
			CellStyle.BORDER_MEDIUM,CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			true);
		addMergedBorderedCell(wb, sheet, 4, 4, Columns.H, Columns.J,
			"Position (Paletten 0 Punkt)", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			true);
		addMergedBorderedCell(wb, sheet, 4, 4, Columns.K, Columns.L,
			"Diagonalen", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			true);
		// Ground sizes
		addBorderedCell(wb, sheet, 5, Columns.E, 
			"Länge", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 5, Columns.F, 
			"Breite", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 5, Columns.G, 
			"Höhe", 
			CellStyle.BORDER_THIN,CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		// Position 
		addBorderedCell(wb, sheet, 5, Columns.H, 
			"X", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 5, Columns.I, 
			"Y", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 5, Columns.J, 
			"Z", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		// Diagonals
		addBorderedCell(wb, sheet, 5, Columns.K, 
			"D1", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addBorderedCell(wb, sheet, 5, Columns.L, 
			"D2", 
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		
		addMergedBorderedCell(wb, sheet, 6, 6, Columns.A, Columns.B,
			"Geometriedarstellung", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		addMergedBorderedCell(wb, sheet, 6, 6, Columns.C, Columns.D,
			verify.getResult().stream().findFirst().get().nameProperty().get(), 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		
		verify.getSpecification().stream().findFirst().ifPresent(speci ->
		{
			addDataRow(wb, sheet, Columns.E, 6, 
				speci.lengthProperty().get(),
				speci.widthProperty().get(),
				speci.heightProperty().get());
			addDataRow(wb, sheet, Columns.H, 6, 
				speci.xProperty().get(),
				speci.yProperty().get(), 
				speci.zProperty().get());
			addDataRow(wb, sheet, Columns.K, 6, 
				speci.d1Property().get(),
				speci.d2Property().get(),
				Double.NaN);
		});
	}

	protected void addDateTimeWeather(HSSFWorkbook wb, HSSFSheet sheet, CooMeasurement measurement)
	{
		addMergedBorderedCell(wb, sheet, 24, 24, Columns.H, Columns.I,
			"Datum", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN);
		addMergedBorderedCell(wb, sheet, 25, 25, Columns.H, Columns.I,
			"Uhrzeit", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);
		addMergedBorderedCell(wb, sheet, 26, 26, Columns.H, Columns.I,
			"Wetter", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);
		// -> date/time/weather values
		addMergedBorderedCell(wb, sheet, 24, 24, Columns.J, Columns.L,
			measurement.dateProperty().get().toString(), 
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 25, 25, Columns.J, Columns.L,
			measurement.fromProperty().get() + " - " + measurement.toProperty().get(),
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, 
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 26, 26, Columns.J, Columns.L,
			measurement.weatherProperty().get(), 
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.ALIGN_RIGHT);		
	}

	protected void addLux(HSSFWorkbook wb, HSSFSheet sheet)
	{
		addMergedBorderedCell(wb, sheet, 26, 26, Columns.A, Columns.D,
			"Helligkeit der Projektionsumgebung [Lux]",
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM,
			CellStyle.ALIGN_LEFT);
		// -> lux values
		addMergedBorderedCell(wb, sheet, 26, 26, Columns.E, Columns.G,
			"", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_MEDIUM);		
	}

	protected void addProjectionHeight(HSSFWorkbook wb, HSSFSheet sheet)
	{
		addMergedBorderedCell(wb, sheet, 24, 25, Columns.A, Columns.D,
			"Projektionshöhe des Lasers über der Palettenebene [mm]",
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.ALIGN_LEFT);
		// -> projection height value
		addMergedBorderedCell(wb, sheet, 24, 25, Columns.E, Columns.G,
			"3.263", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN);		
	}

	protected void addTargetsToLaser(HSSFWorkbook wb, HSSFSheet sheet)
	{
		addMergedBorderedCell(wb, sheet, 20, 20, Columns.A, Columns.D,
			"", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE);
		addMergedBorderedCell(wb, sheet, 21, 21, Columns.A, Columns.D,
			"Kalibrierung Laser 1 mit Targets",
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE);
		addMergedBorderedCell(wb, sheet, 22, 22, Columns.A, Columns.D,
			"Kalibrierung Laser 2 mit Targets",
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE);
		addMergedBorderedCell(wb, sheet, 23, 23, Columns.A, Columns.D,
			"", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM);
		// values
		addMergedBorderedCell(wb, sheet, 20, 20, Columns.E, Columns.G,
			"", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE);
		addMergedBorderedCell(wb, sheet, 21, 21, Columns.E, Columns.G,
			"1,2,5,6", 
			CellStyle.BORDER_MEDIUM,CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE);
		addMergedBorderedCell(wb, sheet, 22, 22, Columns.E, Columns.G,
			"1,2,5,6", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE);
		addMergedBorderedCell(wb, sheet, 23, 23, Columns.E, Columns.G,
			"", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM);
		// description
		addMergedBorderedCell(wb, sheet, 20, 23, Columns.H, Columns.L,
			"", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);		
	}

	protected void addTargets(HSSFWorkbook wb, HSSFSheet sheet, CooMeasurement measurement)
	{
		addMergedBorderedCell(wb, sheet, 14, 19, Columns.A, Columns.B,
			"Positionen der Targets bezogen auf den Palettennullpunkt", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, 
			CellStyle.ALIGN_LEFT);
		addMergedBorderedCell(wb, sheet, 14, 14, Columns.C, Columns.D,
			"Target 1", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, 
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 15, 15, Columns.C, Columns.D,
			"Target 2", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, 
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 16, 16, Columns.C, Columns.D,
			"Target 3", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 17, 17, Columns.C, Columns.D,
			"Target 4", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 18, 18, Columns.C, Columns.D,
			"Target 5", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, 
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 19, 19, Columns.C, Columns.D,
			"Target 6", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.ALIGN_RIGHT);
		int targetRow = 14;
		for(CooTarget t : measurement.getTargets())
		{
			addDataRow(wb, sheet, Columns.E, targetRow++, 
				t.xProperty().get(),
				t.yProperty().get(),
				t.zProperty().get());
		}
		
		addMergedBorderedCell(wb, sheet, 12, 19, Columns.H, Columns.L, 
			"", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);		
		addResPicture(wb, sheet, 12, 19, Columns.H, Columns.L, "Beispiel_Laser.png");
	}

	protected void addLaserDeviation(HSSFWorkbook wb, HSSFSheet sheet)
	{
		addMergedBorderedCell(wb, sheet, 12, 13, Columns.A, Columns.B,
			"Gesammtabweichung bei Kalibrierung durch CAD-ProSoft 2D",
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, 
			CellStyle.ALIGN_LEFT);
		addMergedBorderedCell(wb, sheet, 12, 12, Columns.C, Columns.D,
			"Laser 1", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE,
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 13, 13, Columns.C, Columns.D,
			"Laser 2", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 12, 12, Columns.E, Columns.G,
			"-", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN);
		addMergedBorderedCell(wb, sheet, 13, 13, Columns.E, Columns.G,
			"-", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_THIN, CellStyle.BORDER_THIN);		
	}

	protected void addPosition(HSSFWorkbook wb, HSSFSheet sheet)
	{
		addMergedBorderedCell(wb, sheet, 9, 11, Columns.A, Columns.B,
			"Z Koordinaten der Palettenebene (für die Korrekturberechnungen)",
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.ALIGN_LEFT);
		addMergedBorderedCell(wb, sheet, 9, 9, Columns.C, Columns.D,
			"Position X0/Y0", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE,
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 10, 10, Columns.C, Columns.D,
			"Position Xmax/ Y0", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 11, 11, Columns.C, Columns.D,
			"Position Xmax/Ymax", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.ALIGN_RIGHT);
		// values
		addMergedBorderedCell(wb, sheet, 9, 9, Columns.E, Columns.G,
			"-", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE);
		addMergedBorderedCell(wb, sheet, 10, 10, Columns.E, Columns.G,
			"-", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE);
		addMergedBorderedCell(wb, sheet, 11, 11, Columns.E, Columns.G,
			"-", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM);
		// description
		addMergedBorderedCell(wb, sheet, 9, 11, Columns.H, Columns.L, 
			"",
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);		
	}

	protected void addReticles(HSSFWorkbook wb, HSSFSheet sheet, CooMeasurement measurement)
	{
		addMergedBorderedCell(wb, sheet, 4, 8, Columns.A, Columns.B, 
			"Freie Stationierung (Gemessene Koordinaten der Zielmarken bezogen auf den Palettennullpunkt)",
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, 
			CellStyle.ALIGN_LEFT);
		addMergedBorderedCell(wb, sheet, 4, 4, Columns.C, Columns.D,
			"Zielmarke 1", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE,
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 5, 5, Columns.C, Columns.D,
			"Zielmarke 2", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, 
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 6, 6, Columns.C, Columns.D,
			"Zielmarke 3", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.ALIGN_RIGHT);
		int reticleRow = 4;
		for(CooReticle r : measurement.getReticles())
		{
			addDataRow(wb, sheet, Columns.E, reticleRow++, 
				r.xProperty().get(),
				r.yProperty().get(),
				r.zProperty().get());
		}
		
		addMergedBorderedCell(wb, sheet, 4, 8, Columns.H, Columns.L, 
			measurement.notesProperty().get(), 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THIN,
			CellStyle.ALIGN_LEFT,
			CellStyle.VERTICAL_TOP);
		// Add the total station
		addMergedBorderedCell(wb, sheet, 7, 7, Columns.C, Columns.D,
			"Position Totalstation",
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, 
			CellStyle.ALIGN_RIGHT);
		addMergedBorderedCell(wb, sheet, 8, 8, Columns.C, Columns.D,
			"Abweichung Totalstation", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM,
			CellStyle.ALIGN_RIGHT);
		CooTotalstation ts = measurement.totalStationProperty().get();
		addDataRow(wb, sheet, Columns.E, 7,
			ts.xProperty().get(), 
			ts.yProperty().get(),
			ts.zProperty().get());
		addDataRow(wb, sheet, Columns.E, 8, 
			ts.deltaXProperty().get(),
			ts.deltaYProperty().get(),
			0);		
	}

	protected void addColumnHeader(HSSFWorkbook wb, HSSFSheet sheet)
	{
		addMergedBorderedCell(wb, sheet, 3, 3, Columns.A, Columns.D, 
			"", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		addBorderedCell(wb, sheet, 3, Columns.E , 
			"X[mm]", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		addBorderedCell(wb, sheet, 3, Columns.F , 
			"Y[mm]", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		addBorderedCell(wb, sheet, 3, Columns.G , 
			"Z[mm]", 
			CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		addMergedBorderedCell(wb, sheet, 3, 3, Columns.H, Columns.L, 
			"Bemerkungen", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			true);		
	}

	protected void addHeader(HSSFWorkbook wb, HSSFSheet sheet, CooStation station)
	{
		addMergedBorderedCell(wb, sheet, 0, 1, Columns.B, Columns.E, 
			"", 
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		addResPicture(wb, sheet, 0, 2, Columns.B, Columns.E, "Beispiel_Logo.png");
		addMergedBorderedCell(wb, sheet, 0, 1, Columns.F, Columns.G,
			"Referenz-Palette",
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			true);
		addMergedBorderedCell(wb, sheet, 0, 1, Columns.H, Columns.H, 
			"40", // TODO add the reference palet to coo measurement data
					// The problem is that we cannot add the palet to measurement data
					// because it is also needed in station and references measurement
					// If we add it to the station we loose connection to measurement
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);
		addMergedBorderedCell(wb, sheet, 0, 1, Columns.I, Columns.J, 
			"Laserstation",
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			true);
		addMergedBorderedCell(wb, sheet, 0, 1, Columns.K, Columns.K, 
			station.nameProperty().get(),
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM,
			CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM);		
	}

	protected void addMergedBorderedCell(HSSFWorkbook wb, HSSFSheet sheet,
					int firstRow, int lastRow, Columns firstColumn, Columns lastColumn,
					String value,
					short borderLeft, short borderRight, short borderTop, short borderBottom)
	{
		addMergedBorderedCell(wb, sheet, firstRow, lastRow, firstColumn, 
			lastColumn, value, borderLeft, borderRight, borderTop, borderBottom, CellStyle.ALIGN_CENTER);
	}
	
	protected void addMergedBorderedCell(HSSFWorkbook wb, HSSFSheet sheet,
					int firstRow, int lastRow, Columns firstColumn, Columns lastColumn,
					String value,
					short borderLeft, short borderRight, short borderTop, short borderBottom, boolean bold)
	{
		addMergedBorderedCell(wb, sheet, firstRow, lastRow, firstColumn, lastColumn, value, borderLeft,
			borderRight, borderTop, borderBottom, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, bold);
	}
	
	protected void addMergedBorderedCell(HSSFWorkbook wb, HSSFSheet sheet,
					int firstRow, int lastRow, Columns firstColumn, Columns lastColumn,
					String value,
					short borderLeft, short borderRight, short borderTop, short borderBottom, short align)
	{
		addMergedBorderedCell(wb, sheet, firstRow, lastRow, firstColumn, lastColumn,
			value, borderLeft, borderRight, borderTop, borderBottom, align, CellStyle.VERTICAL_CENTER);
	}
	
	protected void addMergedBorderedCell(HSSFWorkbook wb, HSSFSheet sheet,
					int firstRow, int lastRow, Columns firstColumn, Columns lastColumn, 
					String value, short borderLeft, short borderRight, short borderTop,
					short borderBottom, short align, short vAlign)
	{
		addMergedBorderedCell(wb, sheet, firstRow, lastRow, firstColumn, lastColumn,
			value, borderLeft, borderRight, borderTop, borderBottom, align, vAlign, false);
	}
	
	protected void addMergedBorderedCell(HSSFWorkbook wb, HSSFSheet sheet,
					int firstRow, int lastRow, Columns firstColumn, Columns lastColumn,
					String value,
					short borderLeft, short borderRight, short borderTop, short borderBottom, short align, short vAlign, boolean bold)
	{
		CellRangeAddress columnHeadDesc= new CellRangeAddress(
			firstRow, // first row
			lastRow, // last row
			firstColumn.ordinal(), // first column
			lastColumn.ordinal() // last column
		);
		sheet.addMergedRegion(columnHeadDesc);
		
		for(int r = firstRow; r <= lastRow; r++)
		{
			// Now create new bordered cells
			for(int c = firstColumn.ordinal(); c <= lastColumn.ordinal(); c++)
			{
				addBorderedCell(wb, sheet, r, Columns.parse(c), value, 
					borderLeft, borderRight, borderTop, borderBottom, align, vAlign, bold);
			}
		}
	}
	
	protected Cell addBorderedCell(HSSFWorkbook wb, HSSFSheet sheet, int row,
					Columns column, String value,
					short borderLeft, short borderRight, short borderTop, short borderBottom)
	{
		return addBorderedCell(wb, sheet, row, column, value, borderLeft,
			borderRight, borderTop, borderBottom, CellStyle.ALIGN_CENTER);
	}
	
	protected Cell addBorderedCell(HSSFWorkbook wb, HSSFSheet sheet, int row,
					Columns column, String value,
					short borderLeft, short borderRight, short borderTop, short borderBottom, short align)
	{
		return addBorderedCell(wb, sheet, row, column, value,
			borderLeft, borderRight, borderTop, borderBottom, align, CellStyle.VERTICAL_CENTER);
	}
	
	protected Cell addBorderedCell(HSSFWorkbook wb, HSSFSheet sheet, int row,
					Columns column, String value,
					short borderLeft, short borderRight, short borderTop, short borderBottom, short align, short vAlign)
	{
		return addBorderedCell(wb, sheet, row, column, value, borderLeft, 
			borderRight, borderTop, borderBottom, align, vAlign, false);
	}

	protected Cell addBorderedCell(HSSFWorkbook wb, HSSFSheet sheet, int row,
					Columns column, String value,
					short borderLeft, short borderRight, short borderTop, short borderBottom, short align, short vAlign, boolean bold)
	{
		CreationHelper crHelper = wb.getCreationHelper();
		CellStyle cellstyle = wb.createCellStyle();
		
		// Create new row if doesn't exist yet
		Row r = Objects.isNull(sheet.getRow(row)) ? sheet.createRow(row) : sheet.getRow(row);
		Cell cell = r.createCell(column.ordinal());
		cell.setCellValue(crHelper.createRichTextString(value));
		cellstyle.setBorderLeft(borderLeft);
		cellstyle.setBorderRight(borderRight);
		cellstyle.setBorderTop(borderTop);
		cellstyle.setBorderBottom(borderBottom);
		cellstyle.setAlignment(align);
		cellstyle.setVerticalAlignment(vAlign);
		cellstyle.setWrapText(true);
		cell.setCellStyle(cellstyle);

		if(bold)
		{
			Font font = wb.createFont();
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		    cellstyle.setFont(font);
		}
		
		return cell;
	}

	protected void addDataRow(Workbook wb, Sheet measurement, Columns firstColumn,
			int row, double d1, double d2, double d3)
	{
		CellStyle cellstyle = wb.createCellStyle();
		Row r = Objects.nonNull(measurement.getRow(row)) ? 
			measurement.getRow(row) : measurement.createRow(row);
		
		if(!Double.isNaN(d1))
		{
			Cell d1Cell = r.createCell(firstColumn.ordinal());
			d1Cell.setCellValue(d1);
			d1Cell.setCellStyle(cellstyle);
		}
		if(!Double.isNaN(d2))
		{
			Cell d2Cell = r.createCell(firstColumn.ordinal() + 1);
			d2Cell.setCellValue(d2);
			d2Cell.setCellStyle(cellstyle);
		}
		if(!Double.isNaN(d3))
		{
			Cell d3Cell = r.createCell(firstColumn.ordinal() + 2);
			d3Cell.setCellValue(d3);
			d3Cell.setCellStyle(cellstyle);
		}
		
		cellstyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellstyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellstyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellstyle.setBorderRight(CellStyle.BORDER_THIN);
		cellstyle.setBorderTop(CellStyle.BORDER_THIN);
	}
	
	protected void addPicture(HSSFWorkbook wb, HSSFSheet sheet,
					int firstRow, int lastRow, Columns firstColumn, Columns lastColumn,
					Image image)
	{
		if(Objects.nonNull(image))
		{
			try
			{
				BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
				ByteArrayOutputStream s = new ByteArrayOutputStream();
				ImageIO.write(bImage, "png", s);
				
				addPicture(wb, sheet, firstRow, lastRow, 
					firstColumn, lastColumn, bImage);
			}
			catch(IOException e)
			{
				CooLog.error("Error while loading image", e);
			}
		}
	}
	
	protected void addResPicture(HSSFWorkbook wb, HSSFSheet sheet,
					int firstRow, int lastRow, Columns firstColumn, Columns lastColumn,
					String imageName)
	{
		try(InputStream inputStream = CooFileUtil.getResourceStream(
			CooFileUtil.ICON_FOLDER + CooFileUtil.IN_JAR_SEPERATOR + imageName))
		{
			BufferedImage img = ImageIO.read(inputStream);
			addPicture(wb, sheet, firstRow, lastRow, 
				firstColumn, lastColumn, img);
		}
		catch (IOException e)
		{
			CooLog.error("Error while loading resource image", e);
		}		
	}

	protected void addPicture(HSSFWorkbook wb, HSSFSheet sheet, int firstRow,
					int lastRow, Columns firstColumn, Columns lastColumn,
					BufferedImage img) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
		ImageIO.write(img, "png", baos);
		baos.flush();
		
		ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
		int pictureIdx = wb.addPicture(baos.toByteArray(),
			Workbook.PICTURE_TYPE_PNG);
		HSSFPicture pic = sheet.createDrawingPatriarch().createPicture(
			anchor, pictureIdx);	
		
		anchor.setCol1(firstColumn.ordinal());
		anchor.setCol2(lastColumn.ordinal());
		anchor.setRow1(firstRow);
		anchor.setRow2(lastRow);
		anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
		
		double imgWidth = pic.getImageDimension().getWidth();
		double imgHeight = pic.getImageDimension().getHeight();
		
		// GET the column width
		float colWidth = 0;
		for(int col = firstColumn.ordinal(); col <= lastColumn.ordinal(); col++)
		{
			colWidth += sheet.getColumnWidthInPixels(col);
		}
		// GET the row height
		float rowHeight = 0;
		for(int row = firstRow; row < lastRow; row++)
		{
			HSSFRow r  = sheet.getRow(row);
			rowHeight += Objects.nonNull(r) ? r.getHeightInPoints() 
				: sheet.getDefaultRowHeightInPoints();
		}
		
		double scaleWidth = colWidth / imgWidth;
		double scaleHeight = rowHeight / imgHeight;
		if(scaleWidth < 0d)
		{
			imgWidth = imgWidth * scaleWidth;
			imgHeight = imgHeight * scaleWidth;
		}
		else if(scaleHeight > 0d)
		{
			imgWidth = imgWidth * scaleHeight;
			imgHeight = imgHeight * scaleHeight;
		}

		int spaceX = (int)((colWidth - imgWidth) / 2);
		int spaceXInExcel = (org.apache.poi.util.Units.EMU_PER_PIXEL / spaceX) * 20;
		anchor.setDx1(spaceXInExcel < 0 ? 0 : spaceXInExcel);
	}

	@Override
	public ExtensionFilter getFileFilter()
	{
		return new FileChooser.ExtensionFilter("Microsoft Excel", "*.*");
	}

	@Override
	public List<Content> getAvailableContent()
	{
		return Arrays.asList(Content.TITLE_PAGE, Content.HEADER_FOOTER,
			Content.TOTALSTATION, Content.REGION_DIVIDING,
			Content.LASER, Content.MEASUREMENTS,
			Content.RETICLES, Content.TARGETS,
			Content.VERIFY_MEASUREMENT, Content.SPECIFICATION, Content.RESULT);
	}
}