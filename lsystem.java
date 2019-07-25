// File name of currently loaded example (rendered on the bottom of the
// screen for your convenience).
String currentFile;


/*****************************************************
 * Place variables for describing the L-System here. *
 * These might include the final expansion of turtle *
 * commands, the step size d, etc.                   *
 *****************************************************/
 /*****************************************************
Press 1-8 to load fractal
Press q to zoom out
Press w to zoom in
Click and Drag to move fractal
sierpinski.txt is 8
  *****************************************************/
 
 
String[] spec;

double d = 10;
double productions, delta;
String axiom, fProd, xProd, yProd;

String expanded;
double xOffset, yOffset;
double startX = 250;
double startY = 250;

ArrayList<Turtle> states = new ArrayList<Turtle>();

ArrayList<Turtle> stack = new ArrayList<Turtle>();

boolean locked;


/*
 * This method is automatically called when ever a new L-System is
 * loaded (either by pressing 1 - 6, or 'o' to open a file dialog).
 *
 * The lines array will contain every line from the selected 
 * description file. You can assume it's a valid description file,
 * so it will have a length of 6:
 *
 *   lines[0] = number of production rule applications
 *   lines[1] = angle increment in degrees
 *   lines[2] = initial axiom
 *   lines[3] = production rule for F (or the string 'nil')
 *   lines[4] = production rule for X (or the string 'nil')
 *   lines[5] = production rule for Y (or the string 'nil')
 */
void processLSystem(String[] lines) {
  // You should write code within this method to process the L-system
  // and produce whatever data structures you'll need to use to
  // draw the L-system when drawLSystem() is called.
  spec = lines;

  //Extracts strings from input file
  productions = Double.parseDouble(spec[0]);
  delta = Double.parseDouble(spec[1]);
  axiom = spec[2];
  fProd = spec[3];
  if (spec[4].equals("nil")) {
    xProd = null;
  } else {
    xProd = spec[4];
  }
  if (spec[5].equals("nil")) {
    yProd = null;
  } else {
    yProd = spec[5];
  }

  //Recursively expands from initial axiom to produce
  //a finalized string of instructions
  String temp = axiom;
  for (int j = 0; j < productions; j++) {
    for (int i = 0; i < temp.length(); i++) {
      char instruction = temp.charAt(i);
      if (instruction == 'F') {
        expanded = expanded + fProd;
      } else if (instruction == 'X') {
        expanded = expanded + xProd;
      } else if (instruction == 'Y') {
        expanded = expanded + yProd;
      } else if (instruction == '+') {
        expanded = expanded + "+";
      } else if (instruction == '-') {
        expanded = expanded + "-";
      } else if (instruction == '[') {
        expanded = expanded + "[";
      } else if (instruction == ']') {
        expanded = expanded + "]";
      } else {
        System.out.println("There is an unrecognized character in expanded");
      }
    }
    temp = expanded;
    if (!(j + 1 == productions)){
      expanded = "";
    }
  }
  
  //Creates a new state for the turtle no matter
  //the instruciton. Produces an in order list
  //of positions and orientations that the turtle
  //must traverse to draw fractals.
  for (int i = 0; i < expanded.length(); i++){
    Turtle newest = states.get(i);
    char instruction = expanded.charAt(i);
    if (instruction == 'F'){
      double curX = newest.getX();
      double curY = newest.getY();
      double newX = curX + d * cos(radians((float)newest.getA()));
      double newY = curY + d * sin(radians((float)newest.getA()));
      states.add(new Turtle(newX, newY, newest.getA()));
    } else if (instruction == 'X'){
      states.add(newest);
    } else if (instruction == 'Y'){
      states.add(newest);
    } else if (instruction == '+'){
      double newA = newest.getA() - delta;
      states.add(new Turtle(newest.getX(), newest.getY(), newA));
    } else if (instruction == '-'){
      double newA = newest.getA() + delta;
      states.add(new Turtle(newest.getX(), newest.getY(), newA));
    } else if (instruction == '['){
      stack.add(newest);
      states.add(newest);
    } else if (instruction == ']'){
      states.add(stack.get(stack.size()-1));
      stack.remove(stack.size()-1);
    } else {
      System.out.println("There is an unrecognized character in expanded");
    }
  }
}

/*
 * This method is called every frame after the background has been
 * cleared to white, but before the current file name is written to
 * the screen.
 *
 * It is not called if there is no loaded file.
 */
void drawLSystem() {
  // Implement your LSystem rendering here 
  for (int i = 0; i < 6; i++) {
    text(spec[i], 10, 20 + 15 * i);
  }
  
  //Uses the in order array of turtle states
  //to draw lines
  for (int i = 0; i < states.size()-1; i++){
    double curX = states.get(i).getX();
    double curY = states.get(i).getY();
    double nextX = states.get(i+1).getX();
    double nextY = states.get(i+1).getY();   
    line((float)curX, (float)curY, (float)nextX, (float)nextY);
  }
}

void setup() {
  size(500, 500);
  System.out.println("Press 1-8 to load fractal\nPress g to zoom out\nPress h to zoom in\nClick and Drag to move fractal\nsierpinski.txt is 8");
}

void draw() {
  background(255);

  if (currentFile != null) {
    drawLSystem();
  }

  fill(0);
  stroke(0);
  strokeWeight(2);
  textSize(15);
  if (currentFile == null) {
    text("Press [1-8] to load an example, or 'o' to open a dialog", 5, 495);
  } else {
    text("Current l-system: " + currentFile, 5, 495);
  }
}

void keyReleased() {
  /*********************************************************
   * The examples loaded by pressing 1 - 6 must be placed  *
   * in the data folder within your sketch directory.      *
   * The same goes for any of your own files you'd like to *
   * load with relative paths.                             *
   *********************************************************/

  if (key == 'o' || key == 'O') {
    // NOTE: This option will not work if you're running the
    // Processing sketch with JavaScript and your browser.
    selectInput("Select a file to load:", "fileSelected");
  } else if (key == '1') {
    loadLSystem("example1.txt");
  } else if (key == '2') {
    loadLSystem("example2.txt");
  } else if (key == '3') {
    loadLSystem("example3.txt");
  } else if (key == '4') {
    loadLSystem("example4.txt");
  } else if (key == '5') {
    loadLSystem("example5.txt");
  } else if (key == '6') {
    loadLSystem("example6.txt");
  } else if (key == '7') {
    loadLSystem("example7.txt");
  } else if (key == '8') {
    loadLSystem("sierpinski.txt");
  } else if (key == 'w'){
    d = d + 1;
    loadLSystem(currentFile);
  } else if (key == 'q'){
    if (d - 1 > 0){
      d = d - 1;
    }
    loadLSystem(currentFile);
  }
  // else modify the above code to include
  // keyboard shortcuts for your own examples
}

import java.io.File;
void fileSelected(File selection) {
  if (selection == null) {
    println("File selection cancelled.");
  } else {
    loadLSystem(selection.getAbsolutePath());
  }
}

//clears all global variables that must reset when 
//a new fractal is being loaded.
void loadLSystem(String filename) {
  expanded = "";
  states.clear();
  states.add(new Turtle(startX,startY,0));

  String[] contents = loadStrings(filename);
  currentFile = filename;
  processLSystem(contents);
}
void mousePressed(){
  locked = true;
  xOffset = mouseX - startX;
  yOffset = mouseY - startY;
}

void mouseDragged(){
  if(locked){
    startX = mouseX-xOffset;
    startY = mouseY-yOffset;
    if (currentFile != null){
      loadLSystem(currentFile);
    }
  }
}

void mouseReleased(){
  locked = false;
}

class Turtle {
  double xPos, yPos, alpha;

  Turtle(double x, double y, double a) {
    xPos = x;
    yPos = y;
    alpha = a;
  }

  double getX() {
    return xPos;
  }

  double getY() {
    return yPos;
  }

  double getA() {
    return alpha;
  }
}
