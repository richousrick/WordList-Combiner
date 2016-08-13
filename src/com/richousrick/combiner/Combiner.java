package com.richousrick.combiner;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;

/**
 * @author Richousrick
 */
public class Combiner {
	
	DecimalFormat df = new DecimalFormat("##.00");
	
	/**
	 * 
	 * Initiates the Combiner class
	 *
	 * @param f1 file to be added first
	 * @param f2 file to be added second
	 * @param out location of the combined file
	 * @throws IOException
	 */
	public Combiner(File f1, File f2, File out) throws IOException {
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		
		System.out.println("Calculating word count");
		
		BufferedReader reader = new BufferedReader(new FileReader(f1));
		
		// used to calculate percentage done
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader = new BufferedReader(new FileReader(f2));
		while (reader.readLine() != null) lines++;
		reader.close();
		
		System.out.println("Wordcount is "+lines);
		
		// holds words currently in the outfile
		HashSet<String> map = new HashSet<String>();
		long pos = 0;
		
		// add first file to the outfile
		BufferedReader bf = new BufferedReader(new FileReader(f1));
		String line;
		while ((line = bf.readLine())!=null){
			// ignore all lines starting with '#'
			if(!line.startsWith("#")){
				map.add(line);
				bw.write(line+"\n");
			}
			// print percentage done
			pos++;
			if(pos%1000000==0){
				System.out.println(pos+"/"+lines+" "+df.format(((double)pos/(double)lines)*100)+"%");
			}
		}
		bf.close();
		
		// add second file to the outfile
		bf = new BufferedReader(new FileReader(f2));
		while ((line = bf.readLine())!=null){
			// ignore all lines already in the outfile and beginning with '#'
			if(!map.contains(line)&&!line.startsWith("#")){
				bw.write(line+"\n");
			}
			// print percentage done
			pos++;
			if(pos%1000000==0){
				System.out.println(pos+"/"+lines+" "+df.format(((double)pos/(double)lines)*100)+"%");
			}
		}
		
		bw.close();
		bf.close();
		System.out.println("Done");
	}

	
	public static void main(String[] args) {
		File f1 = null, f2 = null, out = null;

		// validate input
		if(args.length>2){
			f1 = new File(args[0]);
			if(!f1.isFile()){
				System.out.println("Error: \""+f1.getPath()+"\" is not a valid file");
				return;
			}
			
			f2 = new File(args[1]);
			if(!f2.isFile()){
				System.out.println("Error: \""+f2.getPath()+"\" is not a valid file");
				return;
			}
			out = new File(args[2]);
			if(out.isDirectory()){
				System.out.println("Error: \""+f1.getPath()+"\" is a directory");
				return;
			}
			
			System.out.println("Combining "+f1.getAbsolutePath()+" and "+f2.getAbsolutePath()+" into "+out.getAbsolutePath());
		}else{
			System.out.println("Error: Invalid parameters;\nCorrect use: Combiner [file1] [file2] [fileout]");
			return;
		}
		
		try {
			new Combiner(f1, f2, out);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
