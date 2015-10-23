import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Statistics {

	
	public static void main(String[] args){
		Statistics test = new Statistics();
		test.getAns("E://folder", 2);

		
	}
	private Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
	


	class CalulateClass implements Runnable{
		private File file = null;

		public CalulateClass(File file) {
			super();
			this.file = file;
		}

		@Override
		public void run() {
			InputStreamReader reader = null;
			BufferedReader br = null;
			try {
			
				reader = new InputStreamReader(new FileInputStream(file));
				// ����һ������������reader  
	            br = new BufferedReader(reader); 
	            String line = ""; 
	            while ((line = br.readLine()) != null) {   
	            	String[] words = line.split(" ");//�õ�����
	            	for(String word : words){
	            		if(word.equals("")){
	            			continue;
	            		}
	            		synchronized(map){
		            		if(map.containsKey(word)){
		            			map.put(word, map.get(word)+1);
		            		}else{
		            			map.put(word, 1);
		            		}
	            		}

	            	}
	            } 
	            
	            br.close();
	            reader.close();
	            
			} catch (FileNotFoundException e) {		
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			
		}
		
	}
	
	public void getAns(String filePath, Integer num){
		List<File> allFile = readAllFile(filePath);
		
		ExecutorService executor = Executors.newFixedThreadPool(num);
		
		for(File file : allFile){	
			synchronized(file){
				CalulateClass cc = new CalulateClass(file);
				executor.execute(cc);
			}			
		}
		
		executor.shutdown();
		while (!executor.isTerminated()) {
        }

		for(String key : map.keySet()){
			System.out.println(key+": "+map.get(key));
		}
		
	}
	
	public List<File> readAllFile(String filePath) {  
        File f = null;  
        f = new File(filePath);  
        File[] files = f.listFiles(); // �õ�f�ļ�������������ļ���  
        List<File> list = new ArrayList<File>();  
        for (File file : files) {  
            if(file.isDirectory()) {  
                //��ε�ǰ·�����ļ��У���ѭ����ȡ����ļ����µ������ļ�  
                list.addAll(readAllFile(file.getAbsolutePath()));  
            } else {
            	if(file.getName().endsWith(".txt")){
            		list.add(file);  
            	}
                
            }  
        }  
        return list; 
    }  


}
