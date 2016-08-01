package similarity;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class TF_IDF {
	
	public TF_IDF(){
		
	}
	
	
	public double TF_IDFCalculator(String text, List<String> allTexts, String termToCheck){
    	double tf_idf;
    	
    	tf_idf = tfCalculator(text,termToCheck)*
    			idfCalculator(allTexts,termToCheck); 
    			
    	return tf_idf;
    }

	/**
     * Calculated the tf of term termToCheck
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public double tfCalculator(String text, String termToCheck) 
    {
        double count = 0, textSize = 0, tf = 0;
        
        count = StringUtils.countMatches(text, termToCheck);
        textSize = text.split(" ").length;        		
             
		tf = count / textSize;            
        
        return tf;
    }
     
    /**
     * Calculated idf of term termToCheck
     * @param allTexts : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    public double idfCalculator(List<String> allTexts, String termToCheck) 
    {
        double count = 0;
        double idf = 0;
      	
        for (String ss : allTexts){
        	
        	if (ss.contains(termToCheck))
        		count++;
        }
        
        if(count != 0)
        	idf = 1 + Math.log(allTexts.size() / count);
        else
        	idf = 1;
     
        return idf;
    }     
   
    
    public double[] convertListToArray(java.util.List<Double> tf_idfs){
    	
    	double[] vet = new double[tf_idfs.size()];
    	for(int i = 0; i < vet.length; i++){
    		vet[i] = tf_idfs.get(i);    		
    	}
    	
    	return vet;
    	
    }
    
	
}
