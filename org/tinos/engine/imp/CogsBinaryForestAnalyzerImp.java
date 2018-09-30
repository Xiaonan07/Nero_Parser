package org.tinos.engine.imp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.tinos.engine.CogsBinaryForestAnalyzer;
import org.tinos.fhmm.FHMMList;
import org.tinos.fhmm.NeroFeedHMM;
import org.tinos.fhmm.imp.FMHMMListImp;
import org.tinos.fhmm.imp.NeroFeedHMMImp;
import org.tinos.utils.EngineUtils;
import org.tinos.utils.imp.EngineUtilsImp;
import org.tinos.zabbi.DataString;
public class CogsBinaryForestAnalyzerImp implements  CogsBinaryForestAnalyzer{
	private FHMMList fHMMList;
	private NeroFeedHMM neroFeedHMM;
	private EngineUtils engineUtils;
	@Override
	public void init() throws IOException {
		this.fHMMList = new FMHMMListImp();
		fHMMList.index();
		neroFeedHMM = new NeroFeedHMMImp(); 
		engineUtils = new EngineUtilsImp();
	}
	
	@SuppressWarnings(DataString.RAW_TYPES)
	public List<String> parserString(String input) {
		Map <String, Integer> words = fHMMList.getWords();
		String euclid = fHMMList.getEuclid();
		List<String> output = new ArrayList<>();
		Map <Integer, Map> roots = fHMMList.getRoot();
		int length = input.length();
		int depth = DataString.INT_ZERO;
		int tempLength;
		for(int i = DataString.INT_ZERO; i < length; i += (tempLength == DataString.INT_ZERO ?
				DataString.INT_ONE : tempLength)){
			String temp = DataString.EMPTY_STRING + input.charAt(i);
			temp = neroFeedHMM.getBinaryForestRecurWord(temp, input, i, length, roots, depth);
			if(temp.length() == DataString.INT_ONE){
				output.add(temp);
			}else if(temp.length() == DataString.INT_TWO){
				output.add(temp);
			}else if(temp.length() == DataString.INT_THREE) {
				output = engineUtils.doEuclidCheck(output, euclid, temp);
			}else if(temp.length() == DataString.INT_FOUR){
				output = engineUtils.doSlangCheck(output,words,temp);
			}else{
				for(int j = DataString.INT_ZERO; j < temp.length(); j++) {
					output.add(DataString.EMPTY_STRING + temp.charAt(j));
				}
			}			
			tempLength = temp.length();
		}
		return output;
	}
}

 