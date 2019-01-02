package org.sel.config;

import java.util.HashMap;
import java.util.Map;

public class EtlConfig {
	// 自动匹配字段
	private boolean auto = false;
	private Map<String, String> rc = new HashMap<String, String>();

	public EtlConfig(){
		
	}
	public EtlConfig(boolean auto){
		this.auto = auto;
	}
	
	public void add(String fromCol, String toCol) {
		rc.put(fromCol, toCol);
	}

	public boolean isAuto() {
		return auto;
	}

	public Map<String, String> getRc() {
		return rc;
	}

}
