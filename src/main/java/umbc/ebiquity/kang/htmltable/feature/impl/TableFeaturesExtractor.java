package umbc.ebiquity.kang.htmltable.feature.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmltable.core.HTMLDataTable;
import umbc.ebiquity.kang.htmltable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.htmltable.feature.ITableFeaturesExtractor;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimilarityFactory;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;
import umbc.ebiquity.kang.htmltable.similarity.impl.TableRecordsSimilarityFactory;
import umbc.ebiquity.kang.machinelearning.classification.IFeatureData;

public class TableFeaturesExtractor implements ITableFeaturesExtractor {
	private ITableRecordsSimilarityFactory tableRecordsSimilarityFactory;

	private Map<String, ITableFeatureExtractor> horizontalTableFeatureExtractor;
	private Map<String, ITableFeatureExtractor> verticalTableFeatureExtractor;

	public TableFeaturesExtractor() {
		tableRecordsSimilarityFactory = new TableRecordsSimilarityFactory();
		horizontalTableFeatureExtractor = new LinkedHashMap<String, ITableFeatureExtractor>();
		verticalTableFeatureExtractor = new LinkedHashMap<String, ITableFeatureExtractor>();
	}
	
	public void registerHorizontalTableFeatureExtactor(ITableFeatureExtractor tableFeatureExtractor) {
		horizontalTableFeatureExtractor.put(tableFeatureExtractor.getFeatureName(), tableFeatureExtractor);
	}

	public void registerVerticalTableFeatureExtactor(ITableFeatureExtractor tableFeatureExtractor) {
		verticalTableFeatureExtractor.put(tableFeatureExtractor.getFeatureName(), tableFeatureExtractor);
	}
	
	@Override
	public Set<String> getFeatureNames(){
		Set<String> featureNames = new LinkedHashSet<String>();
		featureNames.addAll(horizontalTableFeatureExtractor.keySet());
		featureNames.addAll(verticalTableFeatureExtractor.keySet());
		return featureNames;
	}

	@Override
	public IFeatureData extract(Element element) {
		String tagName = element.tagName();
		if (!"table".equalsIgnoreCase(tagName))
			throw new IllegalArgumentException("The input element must be a table element");

		Elements elements = element.getElementsByTag("tbody");
		if (!elements.isEmpty()) {
			element = elements.get(0);
		}
		
		HTMLDataTable hDataTable = HTMLDataTable.convertToHorizontalDataTable(element);
		HTMLDataTable vDataTable = HTMLDataTable.convertToVerticalDataTable(element);

		ITableRecordsSimiliartySuite hSuite = tableRecordsSimilarityFactory
				.createTableRecordsSimilairtySuite(hDataTable.getTableRecords());

		ITableRecordsSimiliartySuite vSuite = tableRecordsSimilarityFactory
				.createTableRecordsSimilairtySuite(vDataTable.getTableRecords());

		TableFeatures tableFeatures = new TableFeatures();

		for (ITableFeatureExtractor extractor : horizontalTableFeatureExtractor.values()) {
			Object value = extractor.extractFeatureValue(hDataTable, hSuite);
			tableFeatures.addFeature(extractor.getFeatureName(), value);
		}

		for (ITableFeatureExtractor extractor : verticalTableFeatureExtractor.values()) {
			Object value = extractor.extractFeatureValue(vDataTable, vSuite);
			tableFeatures.addFeature(extractor.getFeatureName(), value);
		}
		return tableFeatures;
	}

}
