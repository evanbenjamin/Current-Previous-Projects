//package museum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/*  There is a museum that is looking to add security to the building.  There are many hallways in the museum and you are supposed to place a guard at the intersection of two or more hallways.  The goal is to place a minimum amount of guards.  The input for this program is cvs file filled with points for a graph.  The input is as follows:  0=[1,2,3,4], 1=[0,5,4,3], etc.  0 represents a vertex on the graph, the array is what other vertices it is connected to.  In the example you can see that 0 is connected to the vertices 1,2,3,4.  This graph is bidirectional and contains thousands of vertices.  When a guard is chosen to watch a vertex, they cover all those edges.  The goal of this assignment is to use a minimum amount of guards, but cover the hallways at the same time, in essence to cover all the edges of the graph.  That is how I thought about this assignment, remove a minimum amount of vertices to cover the most edges.  */

public class Museum {

    public static TreeMap<Integer, ArrayList<Integer>> createMap() throws FileNotFoundException{
        File file = new File("museum.txt");
        
        Scanner s = new Scanner(file);

        TreeMap<Integer, ArrayList<Integer>> tree=new TreeMap<>(); //creates map to store values
        
        String text=s.nextLine();//reads in file to one string
        
        s.close();//closes file
        
        String []tokens=text.split("=");//splits keys from equals in text file
        String [] subtoken;//splits values into array of numbers in a string
        
        for(int i=0;i<tokens.length;i++){
            subtoken=tokens[i].split(","); //creates subtoken for values in token to parse from ,
            String replacement;
            
            if(i==tokens.length-1){
                
                replacement = subtoken[0].replace("[", "");
                subtoken[0]=replacement;//takes off brakets 
                
                replacement = subtoken[subtoken.length-1].replace("]}", "");
                subtoken[subtoken.length-1]=replacement;
                
                
                
            }
            if(i>0 && i!=tokens.length-1){
                
                if(i==tokens.length-1){
                    replacement = subtoken[subtoken.length-1].replace("]}", "");
                    subtoken[subtoken.length-1]=replacement;
                }
                
                
                replacement = subtoken[0].replace("[", "");
                subtoken[0]=replacement;//takes off brakets 

                replacement = subtoken[subtoken.length-2].replace("]", "");
                subtoken[subtoken.length-2]=replacement;

            }
            
            
            
            ArrayList<Integer> branches=new ArrayList<>();//used to convert strings to integers
            
            if(i>0 &&i!=tokens.length-1){
                if(i!=tokens.length-1){//minus one so guard doesnt add the next_child key value
                    for(int k=0;k<(subtoken.length-1);k++){
                        branches.add(Integer.parseInt(subtoken[k].trim()));//adds and removes whitespace
                        //from numbers.  They are put into an array
                    }
                }
                
                else{//used for the last smallest_vertex in the map
                    for(int k=0;k<(subtoken.length);k++){
                        branches.add(Integer.parseInt(subtoken[k].trim()));//adds and removes whitespace
                        //from numbers.  They are put into an array
                    }
                }
            }
            
            if(i==tokens.length-1){
                branches.add(Integer.parseInt(subtoken[0].trim()));
            }


            if(i>0){
                tree.put(i-1, branches);//Puts array of number with corresponding key
            }
        
        }
        
        return tree;
    }
    
    
     public static void outFile(Set<Integer> final_verticies) throws FileNotFoundException{
        
        PrintWriter out = new PrintWriter("guards.txt");

        out.write(final_verticies.toString());
      
        out.close();
        
    }

     public static int getBiggest(TreeMap<Integer, ArrayList<Integer>> tree){
         
         int smallest=0;
         int smallest_key=0;
         
         for(Map.Entry<Integer, ArrayList<Integer>> i: tree.entrySet()){
            
            if(i.getValue().size()>smallest){
                
                smallest=i.getValue().size();
                smallest_key=i.getKey();
                
            }
            
        }
         
         return smallest_key;
         
    }
     
   
    public static int getSmallest(TreeMap<Integer, ArrayList<Integer>> tree){
         
         int smallest=1000;
         int smallest_key=0;
         
         for(Map.Entry<Integer, ArrayList<Integer>> i: tree.entrySet()){
            
            if(i.getValue().size()<smallest){
                
                smallest=i.getValue().size();
                smallest_key=i.getKey();
                
            }
            
        }
         
         return smallest_key;
         
    }

    public static void coverEdges(TreeMap<Integer, ArrayList<Integer>> tree, 
            Set<Integer> final_verticies){
        
        int parent=0;
        
        int parent_index=0;
        
        int child=0;


        while(!tree.isEmpty()){
            
            //gets the smallest vertex in the tree
            int smallest_vertex=getSmallest(tree);
            
            if(tree.get(smallest_vertex).size()==1){
                
                parent = tree.get(smallest_vertex).get(0);
            }
            
            else{
                
                parent=getBiggest(tree);
            }
                //System.out.println(tree.get(smallest_vertex).size());

                //list to use to remove verticies from trees
                ArrayList<Integer> remove_list=new ArrayList<>();

                //add the parent to the final set
                final_verticies.add(parent);

                    //used to remove the parent from the children
                    for(int j=0;j<tree.get(parent).size();j++){

                        //gets the child where the parent lies
                        child= tree.get(parent).get(j);

                        parent_index = tree.get(child).indexOf(parent);

                        //removes the smallest_key from the childs list using the smallest_key index 
                        tree.get(child).remove(parent_index);

                        //if the parent is empty due to removing the smallest_key
                        if(tree.get(child).isEmpty()){                                
                            //remove the parent from the tree
                            remove_list.add(child);
                        }

                    }

                //removes anything added to in the list
                for(int i=0;i<remove_list.size();i++){

                    tree.remove(remove_list.get(i));
                }

                //removes the parent
                tree.remove(parent);
                //System.out.println(final_verticies.size());

        }

    }
    
    
    public static void main(String[] args) throws FileNotFoundException{
        
        
        Set<Integer> final_verticies=new HashSet<>();//holds final result
        
        TreeMap<Integer, ArrayList<Integer>> tree=new TreeMap<>();//holds actual tree graph
        tree=createMap();

        coverEdges(tree, final_verticies);//covers edges by adding 1 to all_edges 
        
        //System.out.println(final_verticies.size());
        
        //System.out.println(final_verticies);
        
        outFile(final_verticies);
   
    }
    
}