package org.sel.init;

import org.sel.data.column.Columns;

public interface SimpleEtlInitialization {
	void after(Columns fromCols, Columns toCols);
	void before();
}
