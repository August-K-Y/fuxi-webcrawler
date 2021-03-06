package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayBuilder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodeConsolidator;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlayConstructor;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteParsedPathsHolder;

public class WebSiteEntityTreesBuilder {

	private IHTMLTreeOverlayBuilder overlayBuilder;
	private IHTMLTreeOverlayRefiner blankNodeResolver;
	private IHTMLTreeOverlayRefiner templateNodePruner;
	
	public WebSiteEntityTreesBuilder() {
		overlayBuilder = new HTMLTreeOverlayConstructor();
		blankNodeResolver = new StandardHTMLTreeBlankNodeConsolidator();
	}

	public void build(IWebSiteParsedPathsHolder webSiteParsedPathsHolder) {

		List<IHtmlDocumentParsedPathsHolder> webpages = webSiteParsedPathsHolder.getHtmlDocumentParsedPathHolders();

		List<IHTMLTreeOverlay> htmlTreeOverlayList = buildOverlays(webpages);

		markTemplates(htmlTreeOverlayList);

		htmlTreeOverlayList = resolveBlanNodeInOverlays(htmlTreeOverlayList);
		
		// TODO: combine html trees and identify relations, concept hierarchies

	}

	private List<IHTMLTreeOverlay> resolveBlanNodeInOverlays(List<IHTMLTreeOverlay> htmlTreeOverlayList) {
		List<IHTMLTreeOverlay> newHtmlTreeOverlayList = new ArrayList<IHTMLTreeOverlay>();
		for(IHTMLTreeOverlay overlay : htmlTreeOverlayList){
			IHTMLTreeOverlay newRoot = blankNodeResolver.refine(overlay);
			newHtmlTreeOverlayList.add(newRoot);
		}
		return newHtmlTreeOverlayList;
	}

	private List<IHTMLTreeOverlay> buildOverlays(List<IHtmlDocumentParsedPathsHolder> webpages) {
		List<IHTMLTreeOverlay> htmlTreeOverlayList = new ArrayList<IHTMLTreeOverlay>(webpages.size());
		for (IHtmlDocumentParsedPathsHolder webpage : webpages) {
			IHTMLTreeOverlay treeOverlay = overlayBuilder.build(webpage);
			htmlTreeOverlayList.add(treeOverlay);
		}
		return htmlTreeOverlayList;
	}

	private void markTemplates(List<IHTMLTreeOverlay> htmlTreeOverlayList) {
	}

}
