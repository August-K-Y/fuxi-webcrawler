package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.IHtmlNode;
import umbc.ebiquity.kang.htmldocument.IHtmlPath;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreePropertyNode;
import umbc.ebiquity.kang.htmldocument.util.BasicValidator;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.translator.IPropertyHeaderTranslator;
import umbc.ebiquity.kang.textprocessing.similarity.impl.EqualStemBoostingLabelSimilarity;
import umbc.ebiquity.kang.textprocessing.similarity.impl.OrderedTokenListSimilarity;

/**
 * This class is to identify property headers from table records based a
 * pre-defined controlled dictionary that typically stores domain-specific
 * properties.
 * 
 * @author yankang
 *
 */
public class DictionaryBasedPropertyHeaderTranslator implements IPropertyHeaderTranslator {
	
	private EqualStemBoostingLabelSimilarity labelSimilarity = new EqualStemBoostingLabelSimilarity(
			new OrderedTokenListSimilarity());
	private double threshold = 0.30;

	@Override
	public List<HTMLTreePropertyNode> translate(List<TableRecord> headerRecords, int skipCellNumber) {

		List<HTMLTreePropertyNode> propertyHeaderNodes = null;
		for (TableRecord record : headerRecords) {
			propertyHeaderNodes = new ArrayList<>(headerRecords.size());

			double totalSim = 0.0;
			List<TableCell> tableCells = record.getTableCells();
			int i = 0;
			for (; i < skipCellNumber; i++) {
				propertyHeaderNodes.add(createPropertyNode(tableCells.get(i)));
			}
			for (; i < tableCells.size(); i++) {
				propertyHeaderNodes.add(createPropertyNode(tableCells.get(i)));
				totalSim += computeSimilarity(extractContent(tableCells.get(i)));
			}
			
			if (totalSim / tableCells.size() >= threshold) {
				return propertyHeaderNodes;
			}
		}
		return null;
	}

	private double computeSimilarity(String text) {
		double max = 0;
		for (String entry : TablePropertyDictionary.getDictionary()) {
			max = Math.max(max, labelSimilarity.computeLabelSimilarity(text, entry));
		}
		return max;
	}
	
	private HTMLTreePropertyNode createPropertyNode(TableCell tableCell) {
		HTMLTreePropertyNode entityNode = new HTMLTreePropertyNode(tableCell.getWrappedElement(), extractContent(tableCell));
		return entityNode;
	}

	private String extractContent(TableCell tableCell) {
		Element element = tableCell.getWrappedElement();
		return element != null ? tableCell.getWrappedElement().text() : "";
	}

	/**
	 * @return the threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(double threshold) {
		BasicValidator.is0to1(threshold);
		this.threshold = threshold;
	}

}