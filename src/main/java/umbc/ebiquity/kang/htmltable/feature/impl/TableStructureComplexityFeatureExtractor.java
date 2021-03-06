package umbc.ebiquity.kang.htmltable.feature.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.IHTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.HTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;

public class TableStructureComplexityFeatureExtractor implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "ComplexityOfStructure";
	}

	@Override
	public Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite) {
		return structureComplexity(dataTable);
	}

	private double structureComplexity(IHTMLDataTable dataTable) {
		double maxComplexityScore = 0.0;
		for (TableRecord record : dataTable.getTableRecords()) {
			for (TableCell cell : record.getTableCells()) {
				Element tcElement = cell.getWrappedElement();
				double temp = calculateStructureComplexity(tcElement);
				if (temp > maxComplexityScore) {
					maxComplexityScore = temp;
				}
			}
		}
		return maxComplexityScore;
	}

	private double calculateStructureComplexity(Element tcElement) {
		return getSpread(tcElement);
	}

	private int getSpread(Element tcElement) {
		int widthOfChildren = 0;
		for (Element child : tcElement.children()) {
			widthOfChildren += getSpread(child);
		}
		return widthOfChildren == 0 ? 1 : widthOfChildren;
	}

}
