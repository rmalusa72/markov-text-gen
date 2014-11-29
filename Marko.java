import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.filechooser.FileFilter;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Marko {

	JFrame frame;
	JTextArea output;
	JPanel buttons;
	JButton fileopen;
	JTextArea path;
	JButton go;
	JFileChooser fc;
	File cur;
	
	public static void main(String[] args) {	
		Marko m = new Marko();
	}
	
	public Marko(){
		
		fc = new JFileChooser();
		FileFilt f = new FileFilt();
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(f);
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,700);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		output = new JTextArea();
		output.setEditable(false);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		JScrollPane scrollpane = new JScrollPane(output);
		frame.add(scrollpane);
		
		buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		buttons.setMaximumSize(new Dimension(500, 100));
		frame.add(buttons);
		
		fileopen = new JButton("Choose File");
		fileopen.addMouseListener(new MouseHandler());
		buttons.add(fileopen, BorderLayout.LINE_START);
		
		path = new JTextArea();
		path.setEditable(false);
		buttons.add(path, BorderLayout.CENTER);
		
		go = new JButton("Go");
		go.addMouseListener(new MouseHandler());
		buttons.add(go, BorderLayout.LINE_END);
		
		frame.setVisible(true);
		
	}
	
	private class MouseHandler implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0){
			if (arg0.getSource().equals(fileopen)){
				int returnval = fc.showOpenDialog(frame);
				if (returnval == JFileChooser.APPROVE_OPTION){
					cur = fc.getSelectedFile();
					path.setText(cur.getName());		
					
				}
			}
			if (arg0.getSource().equals(go)){
				if (cur!=null){
					try {
					output.setText("");
					PrintWriter helper;
					int j = 1;
					while (new File("output" + j + ".txt").exists()){
						j++;
					}
					helper = new PrintWriter ("output" + j + ".txt","UTF-8");
					
					FileReader fr;
					try {
						fr = new FileReader(cur);
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
						
						String currentkey = wordlist[0] + " " + wordlist[1];
						helper.print(wordlist[0] + " " + wordlist[1]);
						helper.flush();
						
						//change 10000 to however many words you want your output to be
						int periodcounter = 0;
						for (int l = 0; l<10000; l++){
							if (preftosuf.containsKey(currentkey)){
								ArrayList<String> listvals = preftosuf.get(currentkey);
								int randval = (int)Math.floor(Math.random()*(float)listvals.size());
								if (listvals.get(randval).contains(".")){periodcounter++;}
								helper.print(" " + (listvals.get(randval)));
								helper.flush();
								output.append(" " + (listvals.get(randval)));
								if (periodcounter==5){
									helper.print("\n\n");
									helper.flush();
									output.append("\n\n");
									periodcounter = 0;
								}
								currentkey = currentkey.substring(currentkey.indexOf(" ") + 1) + " " + listvals.get(randval);
							}else{
								break;
							}
							
						}
					
					
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
					
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}else{
					output.setText("No input file chosen! Please choose one using the Choose File button.");
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class FileFilt extends FileFilter{

		@Override
		public boolean accept(File arg0) {
			if (arg0.getName().toLowerCase().endsWith(".txt")){
				return true;
			}else{
				return false;
			}
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "Only .txt files";
		}

		
	}

}
