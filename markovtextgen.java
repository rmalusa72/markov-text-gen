import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class markovtextgen {

	public static void main (String[] args) throws FileNotFoundException, UnsupportedEncodingException{
		markovtextgen m = new markovtextgen();
	}
	
	public markovtextgen() throws FileNotFoundException, UnsupportedEncodingException{
		
		PrintWriter helper = new PrintWriter ("output.txt","UTF-8");
		
		//change "corpus.txt" to the name of your file
		FileReader fr = new FileReader(new File("corpus.txt"));
		Scanner in = new Scanner(fr);
		
		//get rid of all the newlines and read the input into a single-line string
		String curline = in.nextLine();
		String withoutbreaks = curline;
		
		while (in.hasNext()){
			curline = in.nextLine();
			if (!curline.equals("") && !curline.equals(" ")){
				withoutbreaks = withoutbreaks + " " + curline;
			}
		}
		
		//then split it into an array
		String[] wordlist = withoutbreaks.split(" ");
		
		//this hashmap matches key strings of two words to arraylists of possible following words
		HashMap<String,ArrayList<String>> preftosuf = new HashMap<String,ArrayList<String>>();
		
		//manually add the first couple key-value pairs
		ArrayList<String> first = new ArrayList<String>();
		first.add(wordlist[0]);
		preftosuf.put("",first);
		
		ArrayList<String> second = new ArrayList<String>();
		second.add(wordlist[1]);
		preftosuf.put(wordlist[0], second);
		
		//then iterate through the rest
		for (int i = 0; i<wordlist.length-3;i++){
			String key = wordlist[i] + " " + wordlist[i+1];
			if (preftosuf.containsKey(key)){
				ArrayList<String> val = preftosuf.remove(key);
				val.add(wordlist[i+2]);
				preftosuf.put(key, val);
			}else{
				ArrayList<String> val = new ArrayList<String>();
				val.add(wordlist[i+2]);
				preftosuf.put(key, val);
			}
		}
		
		//currently all outputs start with "when i" because random seemed like too much effort at the time
		helper.print("when I");
		helper.flush();
		String currentkey = "when I";
		
		//change 10000 to however many words you want your output to be
		for (int l = 0; l<10000; l++){
			if (preftosuf.containsKey(currentkey)){
				ArrayList<String> listvals = preftosuf.get(currentkey);
				int randval = (int)Math.floor(Math.random()*(float)listvals.size());
				helper.print(" " + (listvals.get(randval)));
				helper.flush();
				currentkey = currentkey.substring(currentkey.indexOf(" ") + 1) + " " + listvals.get(randval);
			}else{
				break;
			}
			
		}
		
	}
}
