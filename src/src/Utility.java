
import java.io.*;
import java.util.*;


public class Utility {

	public static void write(String fileName, Vector<Vector<Double>> data){
			Vector<String> sample = new Vector<String>();
			FileOutputStream out = null;
			
			
			try{
				out = new FileOutputStream (fileName);
				OutputStreamWriter writer = new OutputStreamWriter(out);
				BufferedWriter buff = new BufferedWriter(writer);
				for(Vector<Double> line : data)
					buff.write(line.toString());
				if (out != null) writer.close();
			}catch(IOException e){
				System.out.println(e);
			}
					
//			for(String curr : sample)
//				data.add(curr.split(","));
//			
//			return data;

		}
}
