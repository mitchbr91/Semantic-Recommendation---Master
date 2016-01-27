package similarity;

import java.util.List;
import java.util.StringTokenizer;

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
    private double tfCalculator(String text, String termToCheck) 
    {
        double count = 0;
        double textSize = 1;
        String word = "";
        double tf = 0;
        
        StringTokenizer st = new StringTokenizer(text);		
        
		while(st.hasMoreElements()){
			
			word = (String) st.nextElement();
			
			if (word.equalsIgnoreCase(termToCheck))
                count++;
			
			textSize++;
		}
        
		tf = count / textSize;            
        
        return tf;
    }
     
    /**
     * Calculated idf of term termToCheck
     * @param allTexts : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    private double idfCalculator(List<String> allTexts, String termToCheck) 
    {
        double count = 0;
        String word = "";
        
        StringTokenizer st;	
        for (String ss : allTexts)
        {
        	st = new StringTokenizer(ss);
        	
        	while(st.hasMoreElements()){
        		word = (String) st.nextElement();
        		
        		 if (word.equalsIgnoreCase(termToCheck))
                 {
                     count++;
                     break;
                 }
        	}
            
        }
        
        return Math.log(allTexts.size() / count);
    }     
   
    
    public double[] convertListToArray(java.util.List<Double> tf_idfs){
    	
    	double[] vet = new double[tf_idfs.size()];
    	for(int i = 0; i < vet.length; i++){
    		vet[i] = tf_idfs.get(i);    		
    	}
    	
    	return vet;
    	
    }
    
	
}
