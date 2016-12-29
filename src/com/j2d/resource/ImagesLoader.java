package com.j2d.resource;
// ImagesLoader.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The Imagesfile and images are stored in "Images/"
   (the IMAGE_DIR constant).

   ImagesFile Formats:

    o <fnm>                     // a single image file

    n <fnm*.ext> <number>       // a series of numbered image files, whose
                                // filenames use the numbers 0 - <number>-1

    s <fnm> <number>            // a strip file (fnm) containing a single row
                                // of <number> images

    g <name> <fnm> [ <fnm> ]*   // a group of files with different names;
                                // they are accessible via  
                                // <name> and position _or_ <fnm> prefix

    and blank lines and comment lines.

    The numbered image files (n) can be accessed by the <fnm> prefix
    and <number>. 

    The strip file images can be accessed by the <fnm>
    prefix and their position inside the file (which is 
    assumed to hold a single row of images).

    The images in group files can be accessed by the 'g' <name> and the
    <fnm> prefix of the particular file, or its position in the group.


    The images are stored as BufferedImage objects, so they will be 
    manipulated as 'managed' images by the JVM (when possible).
*/


import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;

import javax.imageio.*;
//import javax.swing.JPanel;     // for ImageIcon


public class ImagesLoader
{
  //private final static String IMAGE_DIR = "Images/";

  private HashMap<String,ArrayList<BufferedImage>> imagesMap; 
    /* The key is the filename prefix, the object (value) 
       is an ArrayList of BufferedImages */
  private HashMap<String,ArrayList<String>> gNamesMap;
    /* The key is the 'g' <name> string, the object is an
       ArrayList of filename prefixes for the group. This is used to 
       access a group image by its 'g' name and filename. */

  private GraphicsConfiguration gc;
  //images path
  private String imagePath;


  public ImagesLoader(String fnm)
  // begin by loading the images specified in fnm
  { initLoader();
    loadImagesFile(fnm);
  }  // end of ImagesLoader()

  public ImagesLoader()
  {  initLoader();  } 


  private void initLoader()
  {
    imagesMap = new HashMap<String,ArrayList<BufferedImage>>();
    gNamesMap = new HashMap<String,ArrayList<String>>();

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
  }  // end of initLoader()


  private void loadImagesFile(String imgPath)
  /* Formats:
        o <fnm>                     // a single image
        n <fnm*.ext> <number>       // a numbered sequence of images
        s <fnm> <number>            // an images strip
        g <name> <fnm> [ <fnm> ]*   // a group of images 

     and blank lines and comment lines.
  */
  { 
	//File file=new File(fnm);
	//imagePath=file.getParent()+File.separator;
	imagePath=imgPath;
	//imagePath=imagePath.replaceAll("\\\\", "/");
	
    System.out.println("Images path:" + imagePath);
    try {
      //InputStream in = this.getClass().getResourceAsStream(imagePath+"imsInfo.txt");
      FileInputStream in=new FileInputStream(imagePath+"imsInfo.txt");
      //File file=new File(imagePath+"imsInfo.txt");
      //FileInputStream in = new FileInputStream(file);
      BufferedReader br = new BufferedReader( new InputStreamReader(in));
      // BufferedReader br = new BufferedReader( new FileReader(imsFNm));
      String line;
      char ch;
      while((line = br.readLine()) != null) {
        if (line.length() == 0)  // blank line
          continue;
        if (line.startsWith("//"))   // comment
          continue;
        ch = Character.toLowerCase( line.charAt(0) );
        if (ch == 'o')  // a single image
          getFileNameImage(line);
        else if (ch == 'n')  // a numbered sequence of images
          getNumberedImages(line);
        else if (ch == 's')  // an images strip
          getStripImages(line);
        else if (ch == 'g')  // a group of images
          getGroupImages(line);
        else
          System.out.println("Do not recognize line: " + line);
      }
      br.close();
    } 
    catch (IOException e) 
    { System.out.println(e);
      System.exit(1);
    }
  }  // end of loadImagesFile()


  // --------- load a single image -------------------------------

  private void getFileNameImage(String line)
  /* format:
        o <fnm>
  */
  { StringTokenizer tokens = new StringTokenizer(line);

    if (tokens.countTokens() != 2)
      System.out.println("Wrong no. of arguments for " + line);
    else {
      tokens.nextToken();    // skip command label
      System.out.print("o Line: ");
      loadSingleImage( tokens.nextToken() );
    }
  }  // end of getFileNameImage()


  public boolean loadSingleImage(String fnm)
  // can be called directly
  {
    String name = getPrefix(fnm);

    if (imagesMap.containsKey(name)) {
      System.out.println( "Error: " + name + "already used");
      return false;
    }

    BufferedImage bi = loadImage(imagePath+fnm);
    if (bi != null) {
      ArrayList<BufferedImage> imsList = new ArrayList<BufferedImage>();
      imsList.add(bi);
      imagesMap.put(name, imsList);
      System.out.println("  Stored " + name + "/" + fnm);
      return true;
    }
    else
      return false;
  }  // end of loadSingleImage()


  private String getPrefix(String fnm)
  // extract name before '.' of filename
  {
    int posn;
    if ((posn = fnm.lastIndexOf(".")) == -1) {
      System.out.println("No prefix found for filename: " + fnm);
      return fnm;
    }
    else
      return fnm.substring(0, posn);
  } // end of getPrefix()



  // --------- load numbered images -------------------------------

  private void getNumberedImages(String line)
  /* format:
        n <fnm*.ext> <number>
  */
  {
    StringTokenizer tokens = new StringTokenizer(line);

    if (tokens.countTokens() != 3)
      System.out.println("Wrong no. of arguments for " + line);
    else {
      tokens.nextToken();    // skip command label
      System.out.print("n Line: ");

      String fnm = tokens.nextToken();
      int number = -1;
      try {
        number = Integer.parseInt( tokens.nextToken() );
      }
      catch(Exception e)
      { System.out.println("Number is incorrect for " + line);  }

      loadNumImages(fnm, number);
    }
  }  // end of getNumberedImages()



  public int loadNumImages(String fnm, int number)
  /*  Can be called directly.
      fnm is the filename argument in:
           n <f*.ext> <number>
  */
  {
    String prefix = null;
    String postfix = null;
    int starPosn = fnm.lastIndexOf("*");   // find the '*'
    if (starPosn == -1) {
      System.out.println("No '*' in filename: " + fnm);
      prefix = getPrefix(fnm);
    }
    else {   // treat the fnm as prefix + "*" + postfix
      prefix = fnm.substring(0, starPosn);
      postfix = fnm.substring(starPosn+1);
    }

    if (imagesMap.containsKey(prefix)) {
      System.out.println( "Error: " + prefix + "already used");
      return 0;
    }

    return loadNumImages(prefix, postfix, number);
  }  // end of loadNumImages()



  private int loadNumImages(String prefix, String postfix, int number)
  /* Load a series of image files with the filename format
            prefix + <i> + postfix
     where i ranges from 0 to number-1
  */
  { 
    String imFnm;
    BufferedImage bi;
    ArrayList<BufferedImage> imsList = new ArrayList<BufferedImage>();
    int loadCount = 0;

    if (number <= 0) {
      System.out.println("Error: Number <= 0: " + number);
      imFnm = prefix + postfix;
      if ((bi = loadImage(imagePath+imFnm)) != null) {
        loadCount++;
        imsList.add(bi);
        System.out.println("  Stored " + prefix + "/" + imFnm);
      }
    }
    else {   // load prefix + <i> + postfix, where i = 0 to <number-1>
      System.out.print("  Adding " + prefix + "/" + 
                           prefix + "*" + postfix + "... ");
      for(int i=0; i < number; i++) {
        imFnm = prefix + i + postfix;
        if ((bi = loadImage(imagePath+imFnm)) != null) {
          loadCount++;
          imsList.add(bi);
          System.out.print(i + " ");
        }
      }
      System.out.println();
    }

    if (loadCount == 0)
      System.out.println("No images loaded for " + prefix);
    else 
      imagesMap.put(prefix, imsList);

    return loadCount;
  }  // end of loadNumImages()



  // --------- load image strip -------------------------------

  private void getStripImages(String line)
  /* format:
        s <fnm> <number>
  */
  {
    StringTokenizer tokens = new StringTokenizer(line);

    if (tokens.countTokens() != 3)
      System.out.println("Wrong no. of arguments for " + line);
    else {
      tokens.nextToken();    // skip command label
      System.out.print("s Line: ");

      String fnm = tokens.nextToken();
      int number = -1;
      try {
        number = Integer.parseInt( tokens.nextToken() );
      }
      catch(Exception e)
      { System.out.println("Number is incorrect for " + line);  }

      loadStripImages(fnm, number);
    }
  }  // end of getStripImages()



  public int loadStripImages(String fnm, int number)
  /* Can be called directly, to load a strip file, <fnm>,
     holding <number> images.
  */
  {
    String name = getPrefix(fnm);
    if (imagesMap.containsKey(name)) {
      System.out.println( "Error: " + name + "already used");
      return 0;
    }
    // load the images into an array
    BufferedImage[] strip = loadStripImageArray(fnm, number);
    if (strip == null)
      return 0;

    ArrayList<BufferedImage> imsList = new ArrayList<BufferedImage>();
    int loadCount = 0;
    System.out.print("  Adding " + name + "/" + fnm + "... ");
    for (int i=0; i < strip.length; i++) {
      loadCount++;
      imsList.add(strip[i]);
      System.out.print(i + " ");
    }
    System.out.println();

    if (loadCount == 0)
      System.out.println("No images loaded for " + name);
    else 
      imagesMap.put(name, imsList);

    return loadCount;
  }  // end of loadStripImages()



  // ------ grouped filename seq. of images ---------


  private void getGroupImages(String line)
  /* format:
        g <name> <fnm>  [ <fnm> ]*
  */
  { StringTokenizer tokens = new StringTokenizer(line);

    if (tokens.countTokens() < 3)
      System.out.println("Wrong no. of arguments for " + line);
    else {
      tokens.nextToken();    // skip command label
      System.out.print("g Line: ");

      String name = tokens.nextToken();

      ArrayList<String> fnms = new ArrayList<String>();
      fnms.add( tokens.nextToken() );  // read filenames
      while (tokens.hasMoreTokens())
        fnms.add( tokens.nextToken() );

      loadGroupImages(name, fnms);
    }
  }  // end of getGroupImages()



  public int loadGroupImages(String name, ArrayList<String> fnms)
  /* Can be called directly to load a group of images, whose
     filenames are stored in the ArrayList <fnms>. They will
     be stored under the 'g' name <name>.
  */
  {
    if (imagesMap.containsKey(name)) {
      System.out.println( "Error: " + name + "already used");
      return 0;
    }

    if (fnms.size() == 0) {
      System.out.println("List of filenames is empty");
      return 0;
    }

    BufferedImage bi;
    ArrayList<String> nms = new ArrayList<String>();
    ArrayList<BufferedImage> imsList = new ArrayList<BufferedImage>();
    String nm, fnm;
    int loadCount = 0;

    System.out.println("  Adding to " + name + "...");
    System.out.print("  ");
    for (int i=0; i < fnms.size(); i++) {    // load the files
      fnm = (String) fnms.get(i);
      nm = getPrefix(fnm);
      if ((bi = loadImage(imagePath+fnm)) != null) {
        loadCount++;
        imsList.add(bi);
        nms.add( nm );
        System.out.print(nm + "/" + fnm + " ");
      }
    }
    System.out.println();

    if (loadCount == 0)
      System.out.println("No images loaded for " + name);
    else {
      imagesMap.put(name, imsList);
      gNamesMap.put(name, nms);
    }

    return loadCount;
  }  // end of loadGroupImages()


  public int loadGroupImages(String name, String[] fnms)
  // supply the group filenames in an array
  {  
    ArrayList<String> al = new ArrayList<String>( Arrays.asList(fnms) );
    return loadGroupImages(name, al);  
  }



  // ------------------ access methods -------------------

  public BufferedImage getImage(String name)
  /* Get the image associated with <name>. If there are
     several images stored under that name, return the 
     first one in the list.
  */
  {
    ArrayList<BufferedImage> imsList = imagesMap.get(name);
    if (imsList == null) {
      System.out.println("No image(s) stored under " + name);  
      return null;
    }

    // System.out.println("Returning image stored under " + name);  
    return (BufferedImage) imsList.get(0);
  }  // end of getImage() with name input;



  public BufferedImage getImage(String name, int posn)
  /* Get the image associated with <name> at position <posn>
    in its list. If <posn> is < 0 then return the first image
    in the list. If posn is bigger than the list's size, then
    calculate its value modulo the size.
  */
  {
    ArrayList<BufferedImage> imsList = imagesMap.get(name);
    if (imsList == null) {
      System.out.println("No image(s) stored under " + name);  
      return null;
    }

    int size = imsList.size();
    if (posn < 0) {
      // System.out.println("No " + name + " image at position " + posn +
      //                      "; return position 0"); 
      return (BufferedImage) imsList.get(0);   // return first image
    }
    else if (posn >= size) {
      // System.out.println("No " + name + " image at position " + posn); 
      int newPosn = posn % size;   // modulo
      // System.out.println("Return image at position " + newPosn); 
      return (BufferedImage) imsList.get(newPosn);
    }

    // System.out.println("Returning " + name + " image at position " + posn);  
    return (BufferedImage) imsList.get(posn);
  }  // end of getImage() with posn input;



  public BufferedImage getImage(String name, String fnmPrefix)
  /* Get the image associated with the group <name> and filename
     prefix <fnmPrefix>. 
  */
  {
    ArrayList<BufferedImage> imsList = imagesMap.get(name);
    if (imsList == null) {
      System.out.println("No image(s) stored under " + name);  
      return null;
    }

    int posn = getGroupPosition(name, fnmPrefix);
    if (posn < 0) {
      // System.out.println("Returning image at position 0"); 
      return (BufferedImage) imsList.get(0);   // return first image
    }

    // System.out.println("Returning " + name + 
    //                        " image with pair name " + fnmPrefix);  
    return (BufferedImage) imsList.get(posn);
  }  // end of getImage() with fnmPrefix input;



  private int getGroupPosition(String name, String fnmPrefix)
  /* Search the hashmap entry for <name>, looking for <fnmPrefix>.
     Return its position in the list, or -1.
  */
  {
    ArrayList<String> groupNames = gNamesMap.get(name);
    if (groupNames == null) {
      System.out.println("No group names for " + name);  
      return -1;
    }

    String nm;
    for (int i=0; i < groupNames.size(); i++) {
      nm = (String) groupNames.get(i);
      if (nm.equals(fnmPrefix))
        return i;          // the posn of <fnmPrefix> in the list of names
    }

    System.out.println("No " + fnmPrefix + 
                  " group name found for " + name);  
    return -1;
  }  // end of getGroupPosition()



  public ArrayList<BufferedImage> getImages(String name)
  // return all the BufferedImages for the given name
  {
    ArrayList<BufferedImage> imsList = imagesMap.get(name);
    if (imsList == null) {
      System.out.println("No image(s) stored under " + name);  
      return null;
    }

    System.out.println("Returning all images stored under " + name);  
    return imsList;
  }  // end of getImages();


  public boolean isLoaded(String name)
  // is <name> a key in the imagesMap hashMap?
  {
    ArrayList<BufferedImage> imsList = imagesMap.get(name);
    if (imsList == null)
      return false;
    return true;
  }  // end of isLoaded()


  public int numImages(String name)
  // how many images are stored under <name>?
  {
    ArrayList<BufferedImage> imsList = imagesMap.get(name);
    if (imsList == null) {
      System.out.println("No image(s) stored under " + name);  
      return 0;
    }
    return imsList.size();
  } // end of numImages()


  // ------------------- Image Input ------------------

  /* There are three versions of loadImage() here! They use:
           ImageIO   // the preferred approach
           ImageIcon
           Image
     We assume that the BufferedImage copy required an alpha
     channel in the latter two approaches.
  */

   public BufferedImage loadImage(String fnm) 
   /* Load the image from <fnm>, returning it as a BufferedImage
      which is compatible with the graphics device being used.
      Uses ImageIO.
   */
   {
     try {
    	 //System.out.println("!!!!!!!!"+fnm);
    	 FileInputStream in=new FileInputStream(fnm);
       BufferedImage im =  ImageIO.read(in);// getClass().getResource(fnm) 
       // An image returned from ImageIO in J2SE <= 1.4.2 is 
       // _not_ a managed image, but is after copying!

       int transparency = im.getColorModel().getTransparency();
       BufferedImage copy =  gc.createCompatibleImage(
                                im.getWidth(), im.getHeight(),
		                        transparency );
       // create a graphics context
       Graphics2D g2d = copy.createGraphics();
       // g2d.setComposite(AlphaComposite.Src);

       // reportTransparency(IMAGE_DIR + fnm, transparency);

       // copy image
       g2d.drawImage(im,0,0,null);
       g2d.dispose();
       return copy;
     } 
     catch(IOException e) {
       System.out.println("Load Image error for " +
                     fnm + ":\n" + e); //IMAGE_DIR + "/" + 
       return null;
     }
  } // end of loadImage() using ImageIO


/*  private void reportTransparency(String fnm, int transparency)
  {
    System.out.print(fnm + " transparency: ");
    switch(transparency) {
      case Transparency.OPAQUE:
        System.out.println("opaque");
        break;
      case Transparency.BITMASK:
        System.out.println("bitmask");
        break;
      case Transparency.TRANSLUCENT:
        System.out.println("translucent");
        break;
      default:
        System.out.println("unknown");
        break;
    } // end switch
  }*/  // end of reportTransparency()



  /*private BufferedImage loadImage2(String fnm)
    Load the image from <fnm>, returning it as a BufferedImage.
      Uses ImageIcon.
   
  { ImageIcon imIcon = new ImageIcon(
                      getClass().getResource(fnm) );//IMAGE_DIR + 
    if (imIcon == null)
      return null;

    int width = imIcon.getIconWidth();
    int height = imIcon.getIconHeight();
    Image im = imIcon.getImage();

    return makeBIM(im, width, height);
  } */ // end of loadImage() using ImageIcon


  /*private BufferedImage makeBIM(Image im, int width, int height)
  // make a BufferedImage copy of im, assuming an alpha channel
  {
    BufferedImage copy = new BufferedImage(width, height, 
                                        BufferedImage.TYPE_INT_ARGB);
    // create a graphics context
     Graphics2D g2d = copy.createGraphics();
    // g2d.setComposite(AlphaComposite.Src);

    // copy image
    g2d.drawImage(im,0,0,null);
    g2d.dispose();
    return copy;
  }*/  // end of makeBIM()



  /*public BufferedImage loadImage3(String fnm) 
   Load the image from <fnm>, returning it as a BufferedImage.
     Use Image.
  
  { Image im = readImage(fnm);
    if (im == null)
      return null;

    int width = im.getWidth( null );
    int height = im.getHeight( null );

    return makeBIM(im, width, height);
  }*/ // end of loadImage() using Image


/*  private Image readImage(String fnm)
  // load the image, waiting for it to be fully downloaded
  {
    Image image = Toolkit.getDefaultToolkit().getImage(
                     getClass().getResource(fnm) );//IMAGE_DIR + 
    MediaTracker imageTracker = new MediaTracker( new Container() );

    imageTracker.addImage(image, 0);
    try {
      imageTracker.waitForID(0);
    }
    catch (InterruptedException e) {
      return null;
    }
    if (imageTracker.isErrorID(0))
      return null;
    return image;
  } // end of readImage()
*/


  public BufferedImage[] loadStripImageArray(String fnm, int number) 
  /* Extract the individual images from the strip image file, <fnm>.
     We assume the images are stored in a single row, and that there
     are <number> of them. The images are returned as an array of
     BufferedImages
  */
  {
    if (number <= 0) {
      System.out.println("number <= 0; returning null");
      return null;
    }

    BufferedImage stripIm;
    if ((stripIm = loadImage(imagePath+fnm)) == null) {
      System.out.println("Returning null");
      return null;
    }

    int imWidth = (int) stripIm.getWidth() / number;
    int height = stripIm.getHeight();
    int transparency = stripIm.getColorModel().getTransparency();

    BufferedImage[] strip = new BufferedImage[number];
    Graphics2D stripGC;

    // each BufferedImage from the strip file is stored in strip[]
    for (int i=0; i < number; i++) {
      strip[i] =  gc.createCompatibleImage(imWidth, height, transparency);
       
      // create a graphics context
      stripGC = strip[i].createGraphics();
      // stripGC.setComposite(AlphaComposite.Src);

      // copy image
      stripGC.drawImage(stripIm, 
                  0,0, imWidth,height,
                  i*imWidth,0, (i*imWidth)+imWidth,height,
                  null);
      stripGC.dispose();
    } 
    return strip;
  } // end of loadStripImageArray()


}  // end of ImagesLoader class
