//package hardermaze;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/*This programming assignment is supposed to represent a 4d maze that is supposed to entertain a race of aliens.  The maze works like this.  The maze contains 0 to N-1 coordinates represented as (x,y,z,t).  If you pick N=3, the maze will contain 3*3*3*3 coordinates, 81 to be exact.  The coordinates are numbered like counting in any base.  For example, if N=2, the coordinates start as follows: (0,0,0,0), (0,0,0,1), (0,0,1,0), and so on until you get to (1,1,1,1).  If N=3, (0,0,0,0), (0,0,0,1), (0,0,0,2), (0,0,1,0), and so on until you get to (2,2,2,2).  So, how are the aliens able to move throughout the maze that is created.  Each coordinate has 4 axes x, y, z, and t.  Each axis has a wall that can be knocked down, once the wall is knocked down the alien can move to another coordinate in a positive or negative direction.  Aliens can only move to other coordinates that differ in one bit.  For example, if you are at coordinate (0,0,0,1), you can move to coordinates (1,0,0,1), (0,1,0,1), (0,0,1,1), or (0,0,0,0).  You pick a coordinate, and move through its axis.  Again, starting at (0,0,0,1), if I move to (0,0,1,1), I have moved through the z coordinate in the positive direction.  If the alien is at (0,1,0,1) and moves to (0,0,0,1), it has moved through the y axis in the negative direction.  

00 means both positive and negative directions can be traveled to
01 means you can move in the negative direction
10 means you can move in the positive direction
11 means you cannot move in either direction

When the maze first starts, all the walls are up, it is up to you to knock the necessary walls down to  make a maze.  The maze must be in a tree form and there can be no loops/cycles.  You also cannot knock down any outer walls, meaning any axes with numbers 0 or N-1.  The maze also must be random, meaning you can touch every coordinate by starting at a random coordinate.  Here is an example of how an N=2 maze can look like.

Start:
xyzt  x  y  z  t
0000  11 11 11 11 
0001  11 11 11 11
0010  11 11 11 11
0011  11 11 11 11
0100  11 11 11 11
0101  11 11 11 11
0110  11 11 11 11
0111  11 11 11 11
1000  11 11 11 11
1001  11 11 11 11
1010  11 11 11 11
1011  11 11 11 11
1100  11 11 11 11
1101  11 11 11 11
1110  11 11 11 11
1111  11 11 11 11

This is how the maze first starts out, each axes in every coordinate has their walls up in both directions.  Now lets knock some down.

End:

xyzt  x  y  z  t
0000  10 10 10 10 
0001  10 10 10 01
0010  11 11 01 11
0011  11 11 01 11
0100  11 01 11 11
0101  11 01 10 11
0110  10 11 11 11
0111  10 11 01 11
1000  01 11 11 11
1001  01 10 11 11
1010  11 10 11 10
1011  11 11 11 01
1100  11 11 11 10
1101  11 01 11 01
1110  01 01 11 10
1111  01 11 11 01

This small example allows the aliens to visit every coordinate, no outer walls are knocked down, and there are no cycles or loops if you represent this as a tree.  The challenge with this assignment was to do big number with N.  We have to test our program on N=40, this comes out to 2,560,000 coordinates.  
*/ 

public class HarderMaze {

    //function calculates a coordinate either x,y,z, or t based on an entire coordinate, range and base
    public static int calculateCoordinate(int cell, int range, int base){
        int coordinate = (int)Math.floor(cell/range)%base;
        
        return coordinate;
    }
    
    //from a top down view, this calculates coordinates that are related to each other in the negative direction, 
    //these would be less than the original coordinate
    public static int calculateNegativeSisterCell(int cell, int range, int n){
        
        cell=cell-range;
        
        if(cell<0){
            cell+=n;
        }
        return cell;
        
    }
    
        
    //from a top down view, this calculates coordinates that are related to each other in the positive direction, 
    //these would be greater than the original coordinate
    public static int calculatePositiveSisterCell(int cell, int range, int n){
        
        cell=cell+range;
        
        if(cell>=n){
            cell-=n;
        }
        return cell;
    }

    //returns bits specified with key, key represents the coordinate
    public static int getBits(int n, TreeMap<Integer, Integer> bits){
        
        return bits.get(n);  
        
    }

    //range list is for getting the spacing between how the numbers repeat within the coordinates.
    //For example, in base 2, 0 will repeat 8 times in the x coordinate, then switch to 1 and repeat 
    //8 more times.  This pattern is the entire size divided by n for x, divide by n again for y, etc.  
    public static ArrayList<Integer> getRangeList(int n, int ncells){
        
        //calculates x range 
        int x_coordinate=ncells/n; 
        
        //calculates y range 
        int y_coordinate=ncells/n/n; 
        
        //calculates z range   
        int z_coordinate=ncells/n/n/n;
        
        ArrayList<Integer> range_list=new ArrayList<>();//list of ranges to randomly pick from
        
        range_list.add(x_coordinate);
        range_list.add(y_coordinate);
        range_list.add(z_coordinate);
        range_list.add(1);//t_coordinate range is always 1
        
        return range_list;
        
    }
    
    //This function creates a map.  The keys are the coordinates, the values are all the possible
    //coordinates the key is allowed to open to.  
    public static TreeMap<Integer,ArrayList<Integer>> coordinateRelations(int n){
        int ncells=n*n*n*n;

        TreeMap<Integer,ArrayList<Integer>> cells=new TreeMap<>();

        ArrayList<Integer> range_list=getRangeList(n, ncells);

        int child=0;  //represents child cell of name_and_parent
        int parent_bit=0;  //represents parent coordinate

        //loops through all coordinates to match potential openings
        for(int i=0;i<ncells;i++)
        {
            //stores potential openings for i, the parent coordinate 
            ArrayList<Integer> children=new ArrayList<>();
            
            for(int j=0;j<range_list.size();j++){
                
                //used to see what number is at the ith coordinate on the specific coordinate
                parent_bit=calculateCoordinate(i, range_list.get(j), n);

                //You can only have positive openings if the coordinate is 0
                if(parent_bit==0){
                    child=calculatePositiveSisterCell(i, range_list.get(j), ncells); 
                    children.add(child);
                }

                //You can only have negative openings if the coordinate is n-1
                else if(parent_bit==(n-1)){
                    child=calculateNegativeSisterCell(i, range_list.get(j), ncells);
                    children.add(child);
                }

                //If its not 0 or n-1, anything goes
                else{  

                    child=calculatePositiveSisterCell(i, range_list.get(j), ncells);
                    children.add(child);

                    child=calculateNegativeSisterCell(i, range_list.get(j), ncells);
                    children.add(child);


                }
            }

            cells.put(i, children);

        }
        
        return cells;
    }
    
    //this function manipulates a map and arraylist in order to keep the shape of a tree
    public static void removeCells(int root, int child,int ncells, int n, 
            TreeMap<Integer,ArrayList<Integer>> cells,TreeMap<Integer,ArrayList<Integer>> cell_ref, 
            TreeMap<Integer, Integer> bits, ArrayList<Integer> tree){
        
       
        ArrayList<Integer> range_list= getRangeList(n, ncells);
        
        int relative=0;//used to see if the coordinate is related to it in the map created earlier
        
        int child_index=0;//
        
        int direction=0;//used to see if you change in the positive or negative direction
        
        int range=0;//
        
        if(root<child){
            direction=1;//positive direction
        }
        
        else{
            direction=0;//negative direction 
        }
        
        //On the first iteration of this program, you need to remove the parent and child from all 
        //other coordinates or else there will be an error or cycle in the end of the program
        if(tree.size()!=2){
            
            //When the tree size is 2, this means the first 2 coordinates have been put into the 
            //tree list.  Entering this loop is for everything but the first iteration
            if(cells.containsKey(child)){
                if(cells.get(child).contains(root)){
                    int remove_index=cells.get(child).indexOf(root);
                    cells.get(child).remove(remove_index);

                    if(cells.get(child).isEmpty()){
                        cells.remove(child);
                    }
                }
            }
        }
        
        else{
            //This else statement is for the first iteration, it loops through the map
            //to remove the parent from all other coordinates
            for(int i=0;i<cell_ref.get(root).size();i++){
            
                //this map does change, its used as a reference to grab easily instead of 
                //calling the range function to calculate everytime
                relative=cell_ref.get(root).get(i);

                if(cells.containsKey(relative)){

                    int root_index=cells.get(relative).indexOf(root);
                    if(root_index!=-1){
                        cells.get(relative).remove(root_index);
                    }

                    if(cells.get(relative).isEmpty()){
                        cells.remove(relative);

                        if(tree.contains(relative)){
                            int remove_index=tree.indexOf(relative);
                            tree.remove(remove_index);
                        }
                    }


                }
            }
        }
        
       
        //loops through map to remove child from all other coordinates.
        for(int i=0;i<cell_ref.get(child).size();i++){
            
            relative=cell_ref.get(child).get(i);
            
            //since some keys will have more than 5 values, I needed to call the range list and
            //compare witht he difference of the cells 
            if(relative==root){
                range=relative-child;
                range=Math.abs(range);
                
                if(range==range_list.get(0)){
                    range=0;
                }
                
                else if(range==range_list.get(1)){
                    range=1;
                }
                
                else if(range==range_list.get(2)){
                    range=2;
                }
                
                else if(range==range_list.get(3)){
                    range=3;
                }
            }
            
            if(cells.containsKey(relative)){

                child_index=cells.get(relative).indexOf(child);
                if(child_index!=-1){
                    cells.get(relative).remove(child_index);
                }
                
                if(cells.get(relative).isEmpty()){
                    cells.remove(relative);

                    if(tree.contains(relative)){
                        int remove_index=tree.indexOf(relative);
                        tree.remove(remove_index);
                    }
                }
                
                
            }
        }
        
       
        if(range==0){
        //changes bits on the x coordinate
            changeBits(root, child, direction, 0, bits);

        }

        else if(range==1){
            //changes bits on the y coordinate
            changeBits(root, child, direction, 1, bits);

        }

        else if(range==2){
            //changes bits on the z coordinate
            changeBits(root, child, direction, 2, bits);

        }

        else {
            changeBits(root, child, direction, 3, bits);
        }
        
    
    }
    
    //pick one random parent_coordinate, one random child_bit
    
    public static TreeMap openWalls(int n){//This chunk check to see if two coordinateRelations share a wall

        int ncells=n*n*n*n;

        TreeMap<Integer, Integer> bits= new TreeMap<>(); //map with bits to change
    
        //map with coordinates to change
        TreeMap<Integer,ArrayList<Integer>> cells=coordinateRelations(n);
        
        //map with coordinates to use as a template to find ranges and other relative coordinates
        TreeMap<Integer,ArrayList<Integer>> cell_ref=coordinateRelations(n);

        int child=0;  //represents child 

        int child_index=0;//represents child index in tree list
        
        Random rand=new Random();//random object to choose random number from tree list
        
        int root_index=0;//represents root or parent

        int root=0;//represents root or parent in tree list

        
        //The next couple lines are for picking a random key since the list doesnt have anything yet.
        //It uses the key set function from maps to get all the keys, then uses a list to choose 
        //from the keys
        Set<Integer> key_transfer=new HashSet<>();

        ArrayList<Integer> keys=new ArrayList<>();

        key_transfer=cells.keySet();

        keys.addAll(key_transfer);
        
        ArrayList<Integer> tree=new ArrayList<>();

        tree.add(keys.get(rand.nextInt(keys.size())));

        
        while(!cells.isEmpty())
        {
            //chooses random index form tree list
            root_index=rand.nextInt(tree.size());
            
            root=tree.get(root_index);

            //Goes to map and calls key that equals root number to get 
            //a random index from the keys arraylist
            child_index=rand.nextInt(cells.get(root).size());
            
            //gets actual child from index
            child=cells.get(root).get(child_index);
            
            //used to make sure the cells map size and tree list stays generally the same.  
            //You dont want to have more coordinates in the tree list because there could accidentally
            //be something there that was already deleted in the cells map
            if(cells.containsKey(child)){
                tree.add(child);
            }
            
            //calls remove function to manipulate map
            removeCells(root, child, ncells, n, cells, cell_ref, bits, tree);

        }
        
        return bits;
    }
    
    public static ArrayList<Integer> getCoordinateBit(int coordinate){
        
        ArrayList<Integer> values=new ArrayList<>();
       
        //x coordinate values to change specific bits
        //In base 2 its 01111111 and 10111111
        if(coordinate==0){
            
            values.add(127);// x coordinate open negative
            values.add(191);// x coordinate open positive
            
            return values;
        }
        
        //y coordinate values to change specific bits
        //In base 2 its 11011111 and 11101111
        else if(coordinate==1){
            
            values.add(223);//y coordinate open negative
            values.add(239);//y coordinate open positive
            
            return values;
        }
        
        //z coordinate values to change specific bits
        //In base 2 its 11110111 and 11111011
        else if(coordinate==2){
            
            values.add(247);//z coordinate open negative
            values.add(251);//z coordinate open positive

            return values;
        }
        
        //t coordinate values to change specific bits
        //In base 2 its 11111101 and 11111110
        else{

            values.add(253);//t coordinate open negative
            values.add(254);//t coordinate open positive
            
            return values;
        }
    }
 
    public static void changeBits(int parent, int child,
            int direction, int coordinate, TreeMap<Integer, Integer> bits){
        
        ArrayList<Integer> coordinates=getCoordinateBit(coordinate);
        
        //if its an outer wall, only one direction is allowed, this will be taken care of elsewhere
        //when an outer wall is being changed, only the CORRECt direction is passed here
        int string_bit1=0;
        int string_bit2=0;
         
        if(bits.containsKey(parent) && bits.containsKey(child)){
            string_bit1=getBits(parent, bits);/*child_bit*/
            string_bit2=getBits(child, bits);/*child*/
        }
        
        else if(bits.containsKey(parent) && !bits.containsKey(child)){
            string_bit1=getBits(parent, bits);/*child_bit*/
            string_bit2=255;
            bits.put(child, 255);
        }
        
        else if(bits.containsKey(child) && !bits.containsKey(parent)){
            string_bit1=255;
            bits.put(parent, 255);
            string_bit2=getBits(child, bits);/*child*/
            
        }
        
        else if(!bits.containsKey(parent) && !bits.containsKey(child)){
            string_bit1=255;
            string_bit2=255;
        }

        if(direction==0){  
            int changebits=coordinates.get(0);//opens negative side
            string_bit1=string_bit1&changebits;

            int changebits1=coordinates.get(1);//opens positive side
            string_bit2=string_bit2&changebits1;
            
        }

        else if(direction==1){  
            int changebits=coordinates.get(1);//opens positive side
            string_bit1=string_bit1&changebits;
           

            int changebits1=coordinates.get(0);//opens negative side
            string_bit2=string_bit2&changebits1;
        
        }
        
        bits.put(parent, string_bit1);
        bits.put(child, string_bit2);
        
    
    }
    
    public static void outFile(TreeMap<Integer, Integer> bits) throws IOException{
        
        ArrayList<Integer> bitstring=new ArrayList<>();
        
        for(int i=0;i<bits.size();i++){
            bitstring.add(bits.get(i));
        }

        OutputStream out = new FileOutputStream("maze.txt");
        
        
        System.out.println(bitstring.get(0));
        for(int i=0;i<bitstring.size();i++){

            out.write(bitstring.get(i));
            
        }
        
        System.out.println(bitstring);
        

        out.close();
    }
     

    public static void main(String[] args) throws IOException {

        //int n=3;
                
        Scanner s = new Scanner(System.in);
        
        int n=s.nextInt();
        
        //creates a map for the bits to change
        TreeMap<Integer, Integer> bits=new TreeMap<>();

        //calls function to change bits
        bits=openWalls(n);
        
        //prints bit by bit to outfile
        outFile(bits);


    }
    
}
