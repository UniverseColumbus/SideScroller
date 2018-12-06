
public class RunSideScroller{
    private Grid grid;
    private int userRow;
    private int msElapsed;
    private int missileElapsed;
    private int timesGet;
    private int timesAvoid;
    private int keyPress;
    private boolean missileHit;

    public RunSideScroller(){
        grid = new Grid(5, 10);
        userRow = 0;
        msElapsed = 0;
        missileElapsed = 0;
        timesGet = 0;
        timesAvoid = 0;
        keyPress = 0;
        missileHit = false;
        updateTitle();
        grid.setImage(new Location(userRow, 0), "user.gif");
    }

    public int play(){
        while (!isGameOver()){
            grid.pause(100);
            msElapsed += 100;
            updateTitle();
            handleKeyPress();
            int chance = (int)(Math.random()*10);
            if(msElapsed < 20000 && chance <= 10 && msElapsed % 1200 == 0){                
                populateRightEdge();
            }
            else if(msElapsed < 20000 && chance <= 3 && msElapsed % 800 == 0){
                populateRightEdge();
            }
            else if(msElapsed < 20000 && chance == 1 && msElapsed % 400 == 0){
                populateRightEdge();
            }
            if(msElapsed >= 20000 && chance <= 10 && msElapsed % 600 == 0){                
                populateRightEdge();
            }
            else if(msElapsed >= 20000 && chance <= 7 && msElapsed % 400 == 0){
                populateRightEdge();
            }
            else if(msElapsed >= 20000 && chance <= 5 && msElapsed % 200 == 0){
                populateRightEdge();
            }
            if(msElapsed % 300 == 0){
                scrollLeft();
            }
            else if(msElapsed >= 20000 && msElapsed % 200 == 0){
                scrollLeft();
            }                                        
            if(timesGet==30){      
                grid.setImage(new Location(userRow,0), "win.gif");
                isGameOver();
            }
            else if(timesAvoid==15){
                grid.setImage(new Location(userRow,0), "lose.gif");
                isGameOver();
            }
        }
        return msElapsed;
    }

    public void handleKeyPress()
    {
        int key = grid.checkLastKeyPressed();
        //up arrow is key 38
        //down arrow is key 40
        if(userRow-1>=0 && key == 38){
            userRow = userRow - 1;
            grid.setImage(new Location(userRow+1,0), null);
            handleCollision(new Location(userRow,0));            
            grid.setImage(new Location(userRow,0), "user.gif");           
        }
        if(userRow+1<=4 && key == 40){
            userRow = userRow + 1;
            grid.setImage(new Location(userRow-1,0), null);
            handleCollision(new Location(userRow,0));            
            grid.setImage(new Location(userRow,0), "user.gif");            
        }
        populateLeftEdge(key);
    }

    public void populateRightEdge(){
        int avoidRow = (int)(Math.random()*5);
        int avoidRow2 = (int)(Math.random()*5);
        int avoidRow3 = (int)(Math.random()*5);
        int avoidChance = (int)(Math.random()*10);
        int getRow = (int)(Math.random()*5);
        int getChance = (int)(Math.random()*10);
        if(avoidChance >= 1){
            grid.setImage(new Location(avoidRow,9), "avoid.gif");             
        }
        if(avoidChance >= 1){
            grid.setImage(new Location(avoidRow2,9), "avoid.gif");
        }
        if(avoidChance >= 7){
            grid.setImage(new Location(avoidRow3,9), "avoid.gif");
        }
        if(getChance >= 7){
            grid.setImage(new Location(getRow,9), "get.gif");
        }
    }

    public void scrollLeft(){              
        for(int row=0;row<grid.getNumRows();row++){
            for(int col=1;col<grid.getNumCols();col++){                
                String image = grid.getImage(new Location(row,col));
                grid.setImage(new Location(row,col), null);
                grid.setImage(new Location(row,col-1), image);                                
                handleCollision(new Location(userRow,0));
                grid.setImage(new Location(userRow,0), "user.gif");    
            }
        }
    }    

    public void populateLeftEdge(int key){                
        if(keyPress < 30 && key == 32){
            keyPress++;
            handleMissileCollision(new Location(userRow,1));
            if(missileHit == false){
                grid.setImage(new Location(userRow,1), "missile.gif");
                scrollRight(new Location(userRow,1));
            }
            else if(missileHit == true){               
                grid.setImage(new Location(userRow,1), "explosion.gif");
                grid.pause(100);
                grid.setImage(new Location(userRow,1), null);
            }
        }        
    }

    public void scrollRight(Location loc){
        String missile = grid.getImage(loc);        
        for(int row=0;row<grid.getNumRows();row++){        
            for(int col=0;col<grid.getNumCols();col++){                                                
                String image = grid.getImage(new Location(row,col));                
                if(image != null && image.equals(missile)){
                    grid.pause(40);
                    if(col+1<=9){
                        handleMissileCollision(new Location(row,col+1));
                        if(missileHit == false){
                            grid.setImage(new Location(row,col), null);                            
                            grid.setImage(new Location(row,col+1), missile);
                            if(image == grid.getImage(new Location(row,9))){
                                grid.pause(40);
                                grid.setImage(new Location(row,9), null);
                            }
                        }
                        else if(missileHit == true){
                            grid.setImage(new Location(row,col), null);
                            grid.setImage(new Location(row,col+1), "explosion.gif");                            
                            grid.pause(100);
                            grid.setImage(new Location(row,col+1), null);
                        }
                    }                                                                                   
                }
            }         
        }        
    }

    public void handleMissileCollision(Location loc){
        String image = grid.getImage(loc);  
        if(image != null && (image.equals("avoid.gif") || image.equals("get.gif"))){
            grid.setImage((loc), null);
            missileHit = true;
        }
        else{
            missileHit = false;
        }
    }

    public void handleCollision(Location loc){
        int collisionCounter = 0;
        String image = grid.getImage(loc);        
        if(image != null && image.equals("avoid.gif")){
            timesAvoid++;
            grid.setImage(new Location(userRow,0), "ouch.gif");
            grid.pause(400);
            grid.setImage(new Location(userRow,0), "user.gif");
        }
        if(image != null && image.equals("get.gif")){
            timesGet++;
            grid.setImage(new Location(userRow,0), "happy.gif");
            grid.pause(400);
            grid.setImage(new Location(userRow,0), "user.gif");
        }        
    }

    public int getScore(){
        return 0;
    }

    public void updateTitle(){
        grid.setTitle("Points: "+timesGet+"    Damage: "+timesAvoid+"    Shots Left: "+(30-keyPress));
        if(timesGet==30){
            grid.setTitle("You Win!");
        }
        else if(timesAvoid==15){
            grid.setTitle("You Lose!");
        }
    }

    public boolean isGameOver(){
        if(timesGet==30 || timesAvoid==15){
            return true;                       
        }
        else{
            return false;
        }
    }

    public static void test(){
        RunSideScroller game = new RunSideScroller();
        game.play();
    }

    public static void main(String[] args){
        test();
    }
}
