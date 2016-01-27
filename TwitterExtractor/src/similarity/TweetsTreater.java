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
		
		String stopwords = "a, agora, ainda, algu�m, algum, alguma, algumas, alguns, ampla, amplas, amplo, amplos, ante, antes, ao, aos, ap�s, aquela, aquelas, aquele,"
				+ " aqueles, aquilo, as, at�, atrav�s, cada, coisa, coisas, com, como, contra, contudo, da, daquele, daqueles, das, de, dela, delas, dele, deles, depois, dessa, dessas,"
				+ " desse, desses, desta, destas, deste, deste, destes, deve, devem, devendo, dever, dever�, dever�o, deveria, deveriam, devia, deviam, disse, disso, disto, dito, diz,"
				+ " dizem, do, dos, e, �, ela, elas, ele, eles, em, enquanto, entre, era, essa, essas, esse, esses, esta, est�, estamos, est�o, estas, estava, estavam, est�vamos, este, "
				+ "estes, estou, eu, fazendo, fazer, feita, feitas, feito, feitos, foi, for, foram, fosse, fossem, grande, grandes, h�, isso, isto, j�, la, l�, lhe, lhes, lo, mas, me,"
				+ " mesma, mesmas, mesmo, mesmos, meu, meus, minha, minhas, muita, muitas, muito, muitos, na, n�o, �, nada, nao, nas, nem, nenhum, nova, novas, novo, novos"
				+ " nessa, nessas, nesta, nestas, ningu�m, no, nos, n�s,"
				+ " nossa, nossas, nosso, nossos, num, numa, nunca, o, os, ou, outra, outras, outro, outros, para, pela, pelas, pelo, pelos, pequena, pequenas, pequeno, pequenos, per,"
				+ " perante, pode, pude, podendo, poder, poderia, poderiam, podia, podiam, pois, por, por�m, porque, posso, pouca, poucas, pouco, poucos, primeiro, primeiros, pr�pria,"
				+ " pr�prias, pr�prio, pr�prios, quais, qual, quando, quanto, quantos, que, quem, s�o, se, seja, sejam, sem, sempre, sendo, sim, ser�, ser�o, seu, sou, seus, si, sido, s�, sob,"
				+ " sobre, sua, suas, talvez, tamb�m, tampouco, te, tem, tendo, tenha, ter, teu, teus, ti, tido, tinha, tinham, toda, todas, todavia, todo, todos, tu, tua, tuas, tudo,"
				+ " �ltima, �ltimas, �ltimo, �ltimos, um, uma, umas, uns, vendo, ver, vez, vindo, vir, vos, v�s, vcs, vc, t�, ta, neh, eh, nois, n�is, pq, pra, t�o, t�o, tou, tava, tbm, ok";
		
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
		
		//String s="Thats a nice joke 😆😆😆 😛";
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
