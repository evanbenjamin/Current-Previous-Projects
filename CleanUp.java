package cleanup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Scanner;
/*
This assignment was supposed to determine the shortest path amongst a large number of coordinates.  It was again a 4D space with coordinates x,y,z,t.  You pick a random coordinate to start at, then you must traverse the rest of the coordinates to find the shortest path back to the starting coordinate.  The input for this was a large cvs file with coordinates x,y,z,t.  

public class CleanUp {

    public static ArrayList<double[]> parseString() throws FileNotFoundException{
        File file = new File("/Users/BenjaminFamily/Desktop/Current Classes/428/points.txt");
        
        Scanner s = new Scanner(file);
        
        ArrayList< double []> points=new ArrayList<>();
        
        
        
        String text=s.nextLine();
        
        s.close();
        String []tokens=text.split(",");
        
        int points_gap=tokens.length/4;
        
        
        
        
        for(int i=0;i<tokens.length;i++){
            
            if(i==0){
                String replace = tokens[i].replace("[", " ");
                tokens[i]=replace;
                
                replace = tokens[i].replace("(", " ");
                tokens[i]=replace;
                
            }
            
            else if(i==tokens.length-1){
                String replace = tokens[i].replace("]", " ");
                tokens[i]=replace;
                
                replace = tokens[i].replace(")", " ");
                tokens[i]=replace;
            }
            
            else if(tokens[i].contains("[")){
                String replace = tokens[i].replace("[", " ");
                tokens[i]=replace;
            }
            
            else if(tokens[i].contains("]")){
                String replace = tokens[i].replace("]", " ");
                tokens[i]=replace;
            }
            
            else if(tokens[i].contains("(")){
                String replace = tokens[i].replace("(", " ");
                tokens[i]=replace;
            }
            
            else if(tokens[i].contains(")")){
                String replace = tokens[i].replace(")", " ");
                tokens[i]=replace;
            }

        }
        
    
        int k=0;
        int four=4;
        for(int i=0;i<points_gap;i++){
            int j=0;
            
            double [] coordinates=new double [4];
            while(k<four){
                
                coordinates[j]=Double.parseDouble(tokens[k]);
                k++;
                j++;
         
            }
            
            four+=4;

            points.add(coordinates);  
        }
        
        return points;
    }
    
    public static double distance(double [] num, double [] num2){
        
        double distance=Math.sqrt(
                Math.pow(num[0]-num2[0], 2)+ 
                Math.pow(num[1]-num2[1], 2)+ 
                Math.pow(num[2]-num2[2], 2)+ 
                Math.pow(num[3]-num2[3], 2));
        
        
        
        return distance;
        
    }
    
    
    public static void outFile(ArrayList<double []> points) throws FileNotFoundException{
        
        PrintWriter out = new PrintWriter("output.txt");
    
        for(int i=0;i<points.size();i++){
            
            if(i==0){
                out.write("[("+points.get(i)[0]+ ","+ points.get(i)[1]+ ","+
                            points.get(i)[2]+ ","+ points.get(i)[3]+"),");
            }
            
            else if(i==points.size()-1){
                
                out.write("("+points.get(i)[0]+ ","+ points.get(i)[1]+ ","+
                           points.get(i)[2]+ ","+ points.get(i)[3]+")]");
            
            }
            
            else{
                out.write("("+points.get(i)[0]+ ","+ points.get(i)[1]+ ","+
                            points.get(i)[2]+ ","+ points.get(i)[3]+"),");
            }
            
        }
        
        out.close();
        
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        
        ArrayList< double []> points=new ArrayList<>();
        
        ArrayList< double []> visited_points=new ArrayList<>();
        

        points=parseString();
        
  
        
       
  
        double total_distance=0.0;
        
        while(!points.isEmpty()){
            
            int key=0;
            int j=1;
            double distance=0.0;
        
            double smallest=1000;
            
            while(j<points.size()){
                distance=distance(points.get(0), points.get(j));
                //try to do j+1 because youll be comparing 2million extra things that are the same
                if(distance<smallest){
                    smallest=distance;
                    key=j;
                }
                j++;
 
            }
            
            total_distance+=smallest;
            
            
            visited_points.add(points.get(0));
            
            if(points.size()==1){
                total_distance+=distance(points.get(0), visited_points.get(0));
                points.remove(0);
            }
            
            else{
                points.add(0, points.get(key));
                points.remove(key+1);
                points.remove(1);
                }
             
        }
        
        
        
        
        System.out.println(total_distance);
        
        outFile(visited_points);
        
        
        

        
        
    }
    
}
