package me.yekki.coh.bootstrap.structures.tools;

import com.tangosol.util.InvocableMap.Entry;
import com.tangosol.util.processor.AbstractProcessor;

public class SleepingProcessor extends AbstractProcessor{
	@Override
	public Object process(Entry arg0) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}