Insert into Maschine(Maschine,P_Queue,C_Queue,CAD_Pfad,Anzeige_Gruppe, CAD_CAM_Version) Values('LAS1','PPSR_QUE','LC_QUE','D:\Unitechnik\Concrete\Erath\Data\Expo\CAD001',5,101);

Insert Into Maschine_Subsystem(Maschine,Remote_CAD_Pfad,Remote_CAD_Datei,Image_Version,Remote_Image_Pfad,REmote_Image_Datei) Values('LAS1','D:\Unitechnik\Concrete\Erath\Data\Expo\CADPOLL_Laser_1','LaserData.ply','2.0','D:\Unitechnik\Concrete\Erath\Data\Expo\CADPOLL_Laser_1','Image.csv');

Insert Into Maschine_Pt_Typ(Maschine,PT_Typ) values ('LAS1','E');

Insert Into PSR_Variante(Variante,Bezeichnung) Values (11,'Laser_1');

Insert Into PRS_Schablone(Variante,Reihe_Nr,Schablone,Bezeichnung,Geometrie) Values(15,1,1509,'CT alle',1);
Insert Into PRS_Schablone(Variante,Reihe_Nr,Schablone,Bezeichnung,Geometrie) Values(15,2,1519,'CO alle',2);
Insert Into PRS_Schablone(Variante,Reihe_Nr,Schablone,Bezeichnung,Geometrie) Values(15,3,1529,'MP alle',4);

Insert Into Maschine_Produkt_Typ(Maschine,Pt_Typ,Variante) Values('LAS1','E',15);

Insert Into Stamm_LC(Gruppe. Maschine) Values(5,'LAS1');
Insert Into Stamm_LC(Gruppe. Maschine) Values(21,'LAS1');

Insert Into Stamm_Lc_Station (Gruppe. Maschine, LFD_Nr, Station) Values(5,'LAS1',1,2);
Insert Into Stamm_Lc_Station (Gruppe. Maschine, LFD_Nr, Station) Values(5,'LAS1',2,3);
Insert Into Stamm_Lc_Station (Gruppe. Maschine, LFD_Nr, Station) Values(21,'LAS1',1,2);




--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO Maschine ( Maschine, P_Queue, C_Queue, CAD_Pfad, Anzeige_Gruppe, CAD_CAM_Version )
VALUES ('LAS2', 'PPSR_QUE', 'LC_QUE', 'D:\Unitechnik\Concrete\Erath\Data\Expo\CAD002', 5, 102);

Insert Into Maschine_Subsystem(Maschine,Remote_CAD_Pfad,Remote_CAD_Datei,Image_Version,Remote_Image_Pfad,REmote_Image_Datei)
Values('LAS2','D:\Unitechnik\Concrete\Erath\Data\Expo\CADPOLL_Laser_2','LaserData.ply','2.0','D:\Unitechnik\Concrete\Erath\Data\Expo\CADPOLL_Laser_2','Image.csv')

Insert Into Maschine_Pt_Typ(Maschine,PT_Typ)
values ('LAS2','E');


Insert Into PSR_Variante(Variante,Bezeichnung)
Values (12,'Laser_2');