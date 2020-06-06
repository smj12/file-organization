package indexing;
import java.io.*;
import java.util.*;
public class Demo {
private SecondaryIndex[] sI = new SecondaryIndex[110010];
	
    private String accno,Name,addr,phno,acc_type;
	int reccount = 0;

	

public void getData(){
    		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the primary key(acc no): ");
	       accno = scanner.next();
		System.out.println("enter the  name");
		Name = scanner.next();
		System.out.println("Enter the address: ");
		addr = scanner.next();
		System.out.println("Enter the phno: ");
		phno = scanner.next();
		System.out.println("Enter the account type: ");
		acc_type = scanner.next();
		
		
}
public void add(){
        String data=accno +","+ Name +","+  addr +","+ phno +","+ acc_type;

 try{			
			RandomAccessFile recordfile = new RandomAccessFile ("C:\\Users\\Jahnavi\\Downloads\\dctr.csv","rw");
			recordfile.seek(recordfile.length());
			long pos = recordfile.getFilePointer();
			recordfile.writeBytes(data+"\n");
			recordfile.close();
			
			RandomAccessFile indexfile = new RandomAccessFile ("secondary.txt","rw");
			indexfile.seek(indexfile.length());
			indexfile.writeBytes(addr+","+pos+"\n");
			indexfile.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
		
 
}                     
    @SuppressWarnings("resource")
	public void secIndex(){

		String line
                        ,sec = null,pos = null;
		int count = 0;
		int sIIndex = 0;
		reccount=0;
		RandomAccessFile indexfile;
		
		 long starttime =System.nanoTime();
		try {
			indexfile = new RandomAccessFile("secondary.txt","rw");

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
				    sec = st.nextToken();
					 pos = st.nextToken();
                                         
				    }
					sI[sIIndex] = new SecondaryIndex();
					sI[sIIndex].setRecPos(pos);
					sI[sIIndex].setsec(sec);
					reccount++;
					sIIndex++;
                                        if(sIIndex==110010)
                                        {
                                            break;
                                        }
                                }
			} catch (IOException e) {
				
				e.printStackTrace();
				return;
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return;
		} 
		
		System.out.println("total records" + reccount);
		long endtime =System.nanoTime();
        long totaltime=endtime-starttime;       
        System.out.println(totaltime/1000000+"msec");
         
		if (reccount==1) { return;}
		sortIndex();
	}
	
	
	public void sortIndex() {
		SecondaryIndex temp = new SecondaryIndex();
		
		for(int i=0; i<reccount; i++)
		    {	
				for(int j=i+1; j<reccount; j++) {
					if(sI[i].getsec().compareTo(sI[j].getsec())  > 0)
		            {
		                temp.setsec(sI[i].getsec()); 
				        temp.setRecPos(sI[i].getRecPos());
				
			        	sI[i].setsec(sI[j].getsec());
			        	sI[i].setRecPos(sI[j].getRecPos());
				
			        	sI[j].setsec(temp.getsec());
			        	sI[j].setRecPos(temp.getRecPos());
		            }
				}
					
			}	
		
	}
        @SuppressWarnings("resource")
		public void search(){
        	 System.out.println("Enter the type of account to search: ");
             Scanner scanner = new Scanner(System.in);
             String acc_type = scanner.next();
             
             
             int oripos = binarySearch(sI, 0, reccount-1, acc_type);
             
             if (oripos == -1) {
                 System.out.println("Record not found in the record file");
                 return ;
             }
             
             int pos = oripos;
             
             do {
                 readFile(pos);
                 pos--;
                 if (pos < 0) { break;}
             }while(sI[pos].getsec().compareTo(acc_type)==0);
             
             pos = oripos + 1 ;
          // if (pos > reccount-1) { return;}
             
             while(sI[pos].getsec().compareTo(acc_type)==0 && pos > reccount-1) {
                 readFile(pos);
                 pos++;
                // if (pos > reccount-1) { break;}
             }
        }
 public void readFile(int pos) {
            
	 if (pos == -1) {
			System.out.println("Record not found in the record file");
			return ;}
            RandomAccessFile recordfile;
            try {
                recordfile = new RandomAccessFile ("C:\\Users\\Jahnavi\\Downloads\\dctr.csv","rw");
                try {String j1=sI[pos].getRecPos().trim();
                recordfile.seek(Long.parseLong(j1));
                    recordfile.seek(Long.parseLong(sI[pos].getRecPos()));
                    String record = recordfile.readLine();
                    StringTokenizer st = new StringTokenizer(record,",");
                    
                    int count = 0;
                       
                    while (st.hasMoreTokens()){
                             count += 1;
                               if(count==1){
                               String tmp_accno = st.nextToken();
                               System.out.println("prikey/acc no: "+tmp_accno);
                               this.accno = tmp_accno;
                               
                               String tmp_Name = st.nextToken();
                               System.out.println("NAME: "+tmp_Name);
                               this.Name = tmp_Name;
                              
                               
                               String tmp_addr = st.nextToken();
                               System.out.println("address: "+tmp_addr);
                               this.addr= tmp_addr;
                               
                               

                               String tmp_phno = st.nextToken();
                               System.out.println("phone number: "+tmp_phno);
                               this.phno = tmp_phno;
                             
                              
                                 
                               
                                String tmp_acc_type = st.nextToken();
                               System.out.println("account type: "+tmp_acc_type);
                               this.acc_type = tmp_acc_type;
                                
                               
                               
                                 
                                   
                               }
                               else
                                   break;
                       }
                    
                    recordfile.close();
                } 
                    catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                
                }
                                        
                catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
 }

        int binarySearch(SecondaryIndex s[], int l, int r, String Gender) {
    	
    	int mid;
    	while (l<=r) {
            
    		mid = (l+r)/2;
    		if(s[mid].getsec().compareTo(Gender)==0)
    		{
    			return mid;
    			}
    		if(s[mid].getsec().compareTo(Gender)<0)
    		{
    			l = mid + 1;
    		}
    			if(s[mid].getsec().compareTo(Gender)>0) 
    		{
    				r = mid - 1;
    				}
    	}
    	return -1;
    }

  public  void indexing() 
  {
	  long starttime =System.nanoTime();
  
  
 
         try{
        RandomAccessFile hey=new RandomAccessFile("C:\\Users\\Jahnavi\\Downloads\\dctr.csv","rw");
  
    			

        RandomAccessFile indexfile=new RandomAccessFile("secondary.txt","rw");
        String line;
 long       pos=hey.getFilePointer();
        while((line = hey.readLine())!=null)
        {
            if(line.contains("*")) {
	                		continue;
	                	}

            String[] columns=line.split(",");
                                 

                        

         
            indexfile.writeBytes(columns[4]+","+pos+"\n");
            pos=hey.getFilePointer();
        } indexfile.close();
        hey.close();
        long endtime =System.nanoTime();
        long totaltime=endtime-starttime;       
        System.out.println(totaltime/1000000+"msec");
         
       
         }
    
    catch(IOException e)
    {
        System.out.println(e);
    }
  }

 @SuppressWarnings("resource")
public   void delete() throws IOException {
	 indexing();
     
     System.out.println("Enter the accno to delete: ");
     Scanner scanner = new Scanner(System.in);
     String specialization = scanner.next();
     String ans = "n";
     int pos;
     
     int oripos = binarySearch(sI, 0, reccount-1, specialization);
     
     if (oripos == -1) {
         System.out.println("Record not found in the record file");
         return ;
     }
     
     pos = oripos;
     
     do {
         readFile(pos);
         
         System.out.println("Do You Want To delete This Record ?(y/n) ");
         ans = scanner.next();
         
         if (ans.compareTo("y")==0) {
             markDelete(addr, pos);
         }
         pos--;
         if (pos < 0) { break;}
     }while(sI[pos].getsec().compareTo(addr)==0);
         
     
     pos = oripos + 1 ;
     
   //  if (pos > reccount-1) { return;}
     while(sI[pos].getsec().compareTo(addr)==0 && pos > reccount-1){
         readFile(pos);
         
         System.out.println("Do You Want To delete This Record ?(y/n) ");
         ans = scanner.next();
         
         if (ans.compareTo("y")==0) {
             markDelete(addr,pos);
             break;
         }
         pos++;
         if (pos > reccount-1) { break;}
     }
}
 @SuppressWarnings("resource")
public void markDelete(String Gender,int pos) {
     try {
         RandomAccessFile recordfilee = new RandomAccessFile ("C:\\Users\\Jahnavi\\Downloads\\dctr.csv","rw");
         RandomAccessFile indexfilee = new RandomAccessFile ("secondary.txt","rw");
         String j1=sI[pos].getRecPos().trim();
         recordfilee.seek(Long.parseLong(j1));
             recordfilee.seek(Long.parseLong(sI[pos].getRecPos()));
             recordfilee.writeBytes("*");
             System.out.println("Done");
             recordfilee.close();
             String line;
             String indexName;
             long indexPos = 0;
             long delPosi;
             //delPosi = indexfilee.getFilePointer();
             while((line = indexfilee.readLine())!=null) {
                 if(line.contains("*"))
                     continue;
                 StringTokenizer st = new StringTokenizer(line,",");
               delPosi = indexfilee.getFilePointer();
                delPosi = delPosi - (line.length()+2);
                 
                // System.out.println("Delposi: " + delPosi);
                 
                 while(st.hasMoreTokens()) {
                     indexName=st.nextToken();
                     indexPos= Long.parseLong(st.nextToken());
                     
                  //   System.out.println("indexPos: " + indexPos);
                    // System.out.println("getrecpos: " + Long.parseLong(sI[pos].getRecPos()));
                     if(indexName.equals(Gender) && indexPos == Long.parseLong(sI[pos].getRecPos()) ) {
                         indexfilee.seek(delPosi);
                         indexfilee.writeBytes("*");
                         indexfilee.close();
                         System.out.println("Deleted");
                         indexing();
                         return;
                     }
                 }
             }
             }
         
         catch (Exception e) {
             e.printStackTrace();
         }
 }

}
