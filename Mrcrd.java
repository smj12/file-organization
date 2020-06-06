package indexing;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Mrcrd {
private MIndex[] sI = new MIndex[110010];
	
    private String accno,Name,addr,phno,acc_type;
	int reccount = 0;

public void getData(){
    		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("EnteR acc no: ");
		accno = scanner.next();
		System.out.println("Enter the NAME: ");
		 Name = scanner.next();
		System.out.println("Enter the address ");
		addr= scanner.next();
		System.out.println("Enter the phno: ");
		phno= scanner.next();
		System.out.println("Enter the type of aacount  ");
		acc_type = scanner.next();
		
}
public void add(){
        String data=accno+","+ Name +","+ addr +","+ phno +","+ acc_type ;

 try{			
			RandomAccessFile recordfile = new RandomAccessFile ("C:\\Users\\Jahnavi\\Downloads\\dctr.csv","rw");
			recordfile.seek(recordfile.length());
			long pos = recordfile.getFilePointer();
			recordfile.writeBytes(data+"\n");
			recordfile.close();
			
			RandomAccessFile indexfile = new RandomAccessFile ("index.txt","rw");
			indexfile.seek(indexfile.length());
			indexfile.writeBytes(accno+","+pos+"\n");
			indexfile.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
		
 
}                     
    @SuppressWarnings("resource")
    public void priIndex(){

		String line
                        ,Identifier = null,pos = null;
		int count = 0;
		int sIIndex = 0;
		reccount=0;
		RandomAccessFile indexfile;
		 long starttime =System.nanoTime();
		try {
			indexfile = new RandomAccessFile("index.txt","rw");

			try {
				
				while((line = indexfile.readLine())!= null){
                                     if(line.contains("*")) {
	                		continue;
	                	}
					count = 0;
                                                 
                                       
	          
					
					StringTokenizer st = new StringTokenizer(line,",");
					while (st.hasMoreTokens()){
					 count+=1;
					 if(count==1)
				    Identifier = st.nextToken();
					pos = st.nextToken();       
				    }
					sI[sIIndex] = new MIndex();
					sI[sIIndex].setRecPos(pos);
					sI[sIIndex].setIdentifier(Identifier);
					reccount++;
					sIIndex++;
                                        if(sIIndex==110010)
                                        {
                                            break;
                                        }
                                }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} //true tells to append data.
		
		System.out.println("total records" + reccount);
		long endtime =System.nanoTime();
        long totaltime=endtime-starttime;       
        System.out.println(totaltime/1000000+"msec");
		if (reccount==1) { return;}
		sortIndex();
	}
	
	
	
	public void sortIndex() {
		MIndex temp = new MIndex();
		
		for(int i=0; i<reccount; i++)
		    {	
				for(int j=i+1; j<reccount; j++) {
					if(sI[i].getIdentifier().compareTo(sI[j].getIdentifier())  > 0)
		            {
		                temp.setIdentifier(sI[i].getIdentifier()); 
				        temp.setRecPos(sI[i].getRecPos());
				
			        	sI[i].setIdentifier(sI[j].getIdentifier());
			        	sI[i].setRecPos(sI[j].getRecPos());
				
			        	sI[j].setIdentifier(temp.getIdentifier());
			        	sI[j].setRecPos(temp.getRecPos());
		            }
				}
					
			}	
		
	}
        public void search(){
            System.out.println("Enter the acc no to search: ");
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(System.in);
					String Identifier = scanner.next();
					System.out.println(reccount);
					int pos = binarySearch(sI, 0, reccount-1, Identifier);
                                        
					
					if (pos == -1) {
						System.out.println("Record not found in the record file");
						return ;
					}
					
					RandomAccessFile recordfile;
					try {
						recordfile = new RandomAccessFile ("C:\\Users\\Jahnavi\\Downloads\\dctr.csv","rw");
						try {
							String j1=sI[pos].getRecPos().trim();
							recordfile.seek(Long.parseLong(j1));
							recordfile.seek(Long.parseLong(sI[pos].getRecPos()));
							String record = recordfile.readLine();
							StringTokenizer st = new StringTokenizer(record,",");
							
							int count = 0;
                                                        
		               	    
		                	while (st.hasMoreTokens()){
		                		     count+=1;
		                  	    	 if(count==1){
		                  	    	 String tmp_accno= st.nextToken();
                                                 if(tmp_accno.contains("*"))
                                                 {
                                                     System.out.println("it has been deleted");
                                                     break;
                                                 }
						System.out.println("acc no: "+tmp_accno);
		                  	         this.accno = tmp_accno;
		                  	    	
		                  	          String tmp_Name= st.nextToken();
		                     	      System.out.println("NAME: "+tmp_Name);
		                     	      this.Name = tmp_Name;
		                     	       
		                     	       String tmp_addr = st.nextToken();
		                     	       System.out.println("address: "+tmp_addr);
		                     	       this.addr = tmp_addr;
		                  	    	 
		                     	        String tmp_phno = st.nextToken();
		                     	        System.out.println("phno: "+tmp_phno);
		                     	        this.phno = tmp_phno;
		                     	      
		                     	        String tmp_acc_type = st.nextToken();
		                     	        System.out.println("account type: "+tmp_acc_type);
		                     	        this.acc_type= tmp_acc_type;
		                     	     
		                     	           	 }

		                  	    	 else
		                  	    		 break;
		                       }
		                	
						} 
							catch (NumberFormatException e) {
							
							e.printStackTrace();
						} 
						catch (IOException e) {
							
							e.printStackTrace();
						}
						
						
						}
												
	                	catch (FileNotFoundException e) {
						
						e.printStackTrace();
					}
        }
        int binarySearch(MIndex s[], int l, int r, String Identifier) {
    	
    	int mid;
    	while (l<=r) {
            
    		mid = (l+r)/2;
    		if(s[mid].getIdentifier().compareTo(Identifier)==0) {return mid;}
    		if(s[mid].getIdentifier().compareTo(Identifier)<0) l = mid + 1;
    		if(s[mid].getIdentifier().compareTo(Identifier)>0) r = mid - 1;
    	}
    	return -1;
    	
    }

  public  void indexing() 
  {
         try{
        RandomAccessFile hey=new RandomAccessFile("C:\\Users\\Jahnavi\\Downloads\\dctr.csv","rw");
  
    			

        RandomAccessFile indexfile=new RandomAccessFile("index.txt","rw");
        String line;
 long       pos=hey.getFilePointer();
        while((line = hey.readLine())!=null)
        {
            if(line.contains("*")) {
	                		continue;
	                	}

            String  columns[] = line.split(",");
          indexfile.writeBytes(columns[0]  +","+pos+"\n");
            pos=hey.getFilePointer();
        } indexfile.close();
        hey.close();
                
        
         
       
         }
    
    catch(IOException e)
    {
        System.out.println(e);
    }
  }

 public   void delete() throws IOException {
         System.out.println("Enter the acc no to delete record ");
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(System.in);
					String Identifier = scanner.next();
        int pos = binarySearch(sI, 0, reccount-1, Identifier);
					
					if (pos == -1) {
						System.out.println("Record not found in the record file");
						return ;
					}
                                        RandomAccessFile recordfile;
                                        
					
						recordfile = new RandomAccessFile ("C:\\Users\\Jahnavi\\Downloads\\dctr.csv","rw");
						try {String j1=sI[pos].getRecPos().trim();
						recordfile.seek(Long.parseLong(j1));
							recordfile.seek(Long.parseLong(sI[pos].getRecPos()));
                                                        recordfile.writeBytes("*");
                                                        recordfile.close();
                                                
                                                        }    
                                                            
                                             catch (NumberFormatException e) {
							
							e.printStackTrace();
						} 
						catch (IOException e) {
							
							e.printStackTrace();
						}
						
						
						}
							


}
