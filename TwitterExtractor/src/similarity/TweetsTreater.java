package similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ptstemmer.Stemmer;
import ptstemmer.exceptions.PTStemmerException;
import ptstemmer.implementations.OrengoStemmer;

public class TweetsTreater {
		
	private Stemmer stemmer;
	
	public TweetsTreater(){
		
	}
	
	public String eliminateStopWords(String text){
		
		String stopwords = "a, agora, ainda, alguém, algum, alguma, algumas, alguns, ampla, amplas, amplo, amplos, ante, antes, ao, aos, após, aquela, aquelas, aquele,"
				+ " aqueles, aquilo, as, até, através, cada, coisa, coisas, com, como, contra, contudo, da, daquele, daqueles, das, de, dela, delas, dele, deles, depois, dessa, dessas,"
				+ " desse, desses, desta, destas, deste, deste, destes, deve, devem, devendo, dever, deverá, deverão, deveria, deveriam, devia, deviam, disse, disso, disto, dito, diz,"
				+ " dizem, do, dos, e, é, ela, elas, ele, eles, em, enquanto, entre, era, essa, essas, esse, esses, esta, está, estamos, estão, estas, estava, estavam, estávamos, este, "
				+ "estes, estou, eu, fazendo, fazer, feita, feitas, feito, feitos, foi, for, foram, fosse, fossem, grande, grandes, há, isso, isto, já, la, lá, lhe, lhes, lo, mas, me,"
				+ " mesma, mesmas, mesmo, mesmos, meu, meus, minha, minhas, muita, muitas, muito, muitos, na, não, ñ, nada, nao, nas, nem, nenhum, nova, novas, novo, novos"
				+ " nessa, nessas, nesta, nestas, ninguém, no, nos, nós,"
				+ " nossa, nossas, nosso, nossos, num, numa, nunca, o, os, ou, outra, outras, outro, outros, para, pela, pelas, pelo, pelos, pequena, pequenas, pequeno, pequenos, per,"
				+ " perante, pode, pude, podendo, poder, poderia, poderiam, podia, podiam, pois, por, porém, porque, posso, pouca, poucas, pouco, poucos, primeiro, primeiros, própria,"
				+ " próprias, próprio, próprios, quais, qual, quando, quanto, quantos, que, quem, são, se, seja, sejam, sem, sempre, sendo, sim, será, serão, seu, sou, seus, si, sido, só, sob,"
				+ " sobre, sua, suas, talvez, também, tampouco, te, tem, tendo, tenha, ter, teu, teus, ti, tido, tinha, tinham, toda, todas, todavia, todo, todos, tu, tua, tuas, tudo,"
				+ " última, últimas, último, últimos, um, uma, umas, uns, vendo, ver, vez, vindo, vir, vos, vós, vcs, vc, tá, ta, neh, eh, nois, nóis, pq, pra, tão, tão, tou, tava, tbm, ok";
		
		StringTokenizer st = new StringTokenizer(stopwords, ",");
		List<String> stopWordSet = new ArrayList<String>();
		
		String stopword;
		while(st.hasMoreElements()){
			stopword = (String) st.nextElement();			
			stopWordSet.add(stopword.trim());
		}
		
		String normalizedText = "";
				
		
		String word;
		st = new StringTokenizer(text);
		
		while(st.hasMoreElements()){
			word = (String) st.nextElement();
			
			if(!stopWordSet.contains(word) && !word.contains("http") && !word.contains("@"))
				normalizedText += word + " ";
		}
		
		//normalizedTweetSet = generateStemmedWords(normalizedTweetSet);
				
		return normalizedText;
	}
	
	public String generateStemmedWords(String text){
		
		StringTokenizer st;
	
		String token;
		String normalizedText;
		
		st = new StringTokenizer(text);
			
			
		normalizedText = "";
		
		try {
			stemmer = new OrengoStemmer();
			stemmer.enableCaching(1000);   //Optional	
			
			while (st.hasMoreElements()) {
						
				token = (String) st.nextElement();
				normalizedText += stemmer.getWordStem(token) + " ";										
				
			}
					
		} catch (PTStemmerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
				
		return normalizedText;
	}
	
	public String eliminateEmojis(String s){
		
		Matcher matcher;
		Pattern pattern;
		java.util.List<String> matchList;
		
		//String s="Thats a nice joke ðŸ˜†ðŸ˜†ðŸ˜† ðŸ˜›";
	    pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
	                                      Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
	    matcher = pattern.matcher(s);
	    matchList = new ArrayList<String>();

	    while (matcher.find()) {
	        matchList.add(matcher.group());		        
	    }
		
	    for(int j=0; j <matchList.size(); j++){		    	
	    	s = s.replace(matchList.get(j), "");	    	
	        
	    }
		
		return s;
	}
	
	public String eliminatePonctuation(String s){		
		
		return s.replaceAll("\\p{P}", "");
	}
	
	public String eliminateNumbers(String s){
		return s.replaceAll("[0-9]","");
	}

}
