package org.mochou.mymall.wx.service;

import org.mochou.mymall.db.domain.MymallRegion;
import org.mochou.mymall.db.service.MymallRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhy
 * @date 2019-01-17 23:07
 **/
@Component
public class GetRegionService {

	@Autowired
	private MymallRegionService regionService;

	private static List<MymallRegion> mymallRegions;

	protected List<MymallRegion> getMymallRegions() {
		if(mymallRegions==null){
			createRegion();
		}
		return mymallRegions;
	}

	private synchronized void createRegion(){
		if (mymallRegions == null) {
			mymallRegions = regionService.getAll();
		}
	}
}
