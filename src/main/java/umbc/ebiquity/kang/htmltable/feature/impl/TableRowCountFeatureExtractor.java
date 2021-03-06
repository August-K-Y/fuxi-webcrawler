package umbc.ebiquity.kang.htmltable.feature.impl;

import umbc.ebiquity.kang.htmltable.IHTMLDataTable;
import umbc.ebiquity.kang.htmltable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;

public class TableRowCountFeatureExtractor implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "NumberOfRows";
	}

	@Override
	public Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite) {
		return dataTable.getRowCount();
	}

}
