package umbc.ebiquity.kang.websiteparser.support;

import java.util.List;

import umbc.ebiquity.kang.websiteparser.IWebPagePath;
import umbc.ebiquity.kang.websiteparser.impl.WebPagePath;

public interface IWebPageParsedPathsHolder {

	List<IWebPagePath> listWebTagPathsWithTextContent(); 

}
