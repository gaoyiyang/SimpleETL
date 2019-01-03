package org.sel.init;

import org.sel.data.column.Columns;

public interface SimpleEtlInitialization {
	void after();
	void before(Columns fromCols, Columns toCols);
}
