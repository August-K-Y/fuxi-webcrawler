package umbc.ebiquity.kang.htmltable.delimiter.impl;

import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.core.HTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.AbstractClusteringBasedTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;

public class ClusteringBasedVerticalTableHeaderDelimiter extends AbstractClusteringBasedTableHeaderDelimiter {
	
	public ClusteringBasedVerticalTableHeaderDelimiter() {
		super();
	}

	@Override
	protected HTMLDataTable convertToDataTable(Element tableElem) {
		return HTMLDataTable.convertToVerticalDataTable(tableElem);
	}
	
	@Override
	protected HeaderDelimitedTable doResolveTable(List<TableRecord> cluster1, List<TableRecord> cluster2) {
		if (cluster1.get(0).getSequenceNumber() < cluster2.get(0).getSequenceNumber()) {
			if (validateClusterSequence(cluster1, cluster2)) {
				HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.VerticalHeaderTable);
				result.setVerticalHeaderRecords(cluster1);
				result.setVerticalDataRecords(cluster2);
				return result;
			} else {
				HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
				return result;
			}

		} else {
			if (validateClusterSequence(cluster2, cluster1)) {
				HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.VerticalHeaderTable);
				result.setVerticalHeaderRecords(cluster2);
				result.setVerticalDataRecords(cluster1);
				return result;
			} else {
				HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
				return result;
			}
		}
	}

	@Override
	protected HeaderDelimitedTable doResolveTable(List<TableRecord> cluster) {
		HeaderDelimitedTable resolveResult = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.NonHeaderTable);
		resolveResult.setVerticalDataRecords(cluster);
		return resolveResult;
	}

}
