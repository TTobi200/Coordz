package de.gui.view3D.ddd.util;

import java.util.*;
import java.util.function.Consumer;

import de.coordz.data.*;
import de.coordz.data.base.*;

public class CooProjectCrawler
{
	private CooData ROOT;

	private Map<Class<CooData>, List<Consumer<CooData>>> mapClassToActions;

	private CooProjectCrawler(CooData data)
	{
		ROOT = data;
		mapClassToActions = new HashMap<>();
	}

	public CooProjectCrawler(CooProject project)
	{
		this((CooData)project);
	}

	public CooProjectCrawler(CooCustomer customer)
	{
		this((CooData)customer);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends CooData> CooProjectCrawler doForClass(Class<T> clazz, Consumer<T> action)
	{
		List<Consumer<CooData>> list = mapClassToActions.get(clazz);
		if(list == null)
		{
			list = new ArrayList<>();
			mapClassToActions.put((Class)clazz, list);
		}
		list.add((Consumer<CooData>)action);
		return this;
	}

	public void crawl()
	{
		if(ROOT instanceof CooCustomer)
		{
			((CooCustomer)ROOT).getProjects().forEach(project -> crawlProject(project));
		}
		else
		{
			crawlProject((CooProject)ROOT);
		}
	}

	private void crawlProject(CooProject project)
	{
		doActionFor(project);

		for(CooStation station : project.getStations())
		{
			doActionFor(station);

			CooGateway gateWay = station.gatewayProperty().get();
			doActionFor(gateWay);

			for(CooLaser laser : gateWay.getLaser())
			{
				doActionFor(laser);
			}

			CooRegionDividing devide = station.regionDevidingProperty().get();
			doActionFor(devide);

			for(CooLaser laser : devide.getLaser())
			{
				doActionFor(laser);
			}

			CooVerifyMeasurement verify = station.verifyMeasurementProperty().get();
			doActionFor(verify);

			for(CooRectangle rect : verify.getResult())
			{
				doActionFor(rect);
			}
			for(CooRectangle rect : verify.getSpecification())
			{
				doActionFor(rect);
			}

			for(CooMeasurement measurement : station.getMeasurements())
			{
				doActionFor(measurement);

				CooTotalstation totalStation = measurement.totalStationProperty().get();
				doActionFor(totalStation);

				for(CooTarget target : measurement.getTargets())
				{
					doActionFor(target);
				}

				for(CooReticle reticle : measurement.getReticles())
				{
					doActionFor(reticle);
				}
			}
		}
	}

	private void doActionFor(CooData object)
	{
		List<Consumer<CooData>> list = mapClassToActions.get(object.getClass());

		if(list != null)
		{
			list.forEach(cons -> cons.accept(object));
		}
	}
}
