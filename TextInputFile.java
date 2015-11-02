package hsa_ufa;

/**
 * The TextInputFile class reads text from a file. The class
 * contains the same readLine method as BufferedReader and
 * adds methods to read all the Java primitive data types
 * with integrated error checking.  It also handles EOF.
 * <p>
 * Full documentation for the classes in the hsa package available at:
 * <br>
 *                      http://www.holtsoft.com/java/hsa_package.html
 * <p>
 * @author Tom West
 * @version 2.0 99/02/01
 */

import java.io.*;

public class TextInputFile
{
    /**
     * The file name.
     */
    protected String fileName;

    /**
     * Stream used for reading input.
     */
    protected BufferedReader f = null;

    /**
     * Set if the file has been closed.
     */
    protected boolean closed = false;

    /**
     * Indicator whether to write to standard output or a file.
     */
    protected boolean useStandardIO = false;

    /**
     * The end-of-file flag
     */
    protected boolean eofFlag = false;

    /**
     * Constants and variables used by the input line buffer.
     */
    protected static final int EMPTY = -1;
    protected String lineBuffer = "";       // The line of input read in from the file      .
    int lineBufferPtr = 0;                  // The pointer in the line of input.
    int ungotChar = EMPTY;                  // The next character to be read in.

    
    /**
     * Contructor - TextInputFile to read from standard input.
     */
    public TextInputFile ()
    {
 this ("Standard input");
    } // Constructor - TextInputFile (void)


    /**
     * Contructor - TextInputFile to read from a File.
     *
     * @param file - File to be opened.
     */
    public TextInputFile (File file)
    {
 try
 {
     FileReader fileReader = new FileReader (file);
     f = new BufferedReader (fileReader);
 }
 catch (FileNotFoundException e)
 {
     new FatalError ("Unable to open file \"" + fileName + "\"");
     // Never reaches here
 }
 this.fileName = file.getName ();
    } // Constructor - TextInputFile (File)


    /**
     * Contructor - TextInputFile to read from file with specified name.
     *
     * @param fileName - Name of the file to be opened.
     */
    public TextInputFile (String fileName)
    {
 if (fileName.equalsIgnoreCase ("standard input") ||
  fileName.equalsIgnoreCase ("keyboard") ||
  fileName.equalsIgnoreCase ("stdin"))
 {
     f = new BufferedReader (new InputStreamReader (System.in));
     this.fileName = "Standard input";
     useStandardIO = true;
 }
 else
 {
     try
     {
  f = new BufferedReader (new FileReader (new File (fileName)));
     }
     catch (FileNotFoundException e)
     {
  new FatalError ("Unable to open file \"" + fileName + "\"");
  // Never reaches here
     }
     this.fileName = fileName;
 }
    } // Constructor - TextInputFile (String)


    /**
     * Close the file to further reading.
     */
    public void close ()
    {
 if (closed)
 {
     new FatalError ("\"" + fileName + "\" is already closed.");
     // Never reaches here.
 }

 // If writing to standard output, don't close standard out.
 if (useStandardIO)
 {
     useStandardIO = false;
 }
 else
 {
     try
     {
  f.close ();
     }
     catch (IOException e)
     {
  new FatalError ("Close failed: Unable to close \"" + fileName + "\"");
  // Never reaches here.
     }
     f = null;
 }

 closed = true;
 lineBuffer = "";
    } // close (void)


    /**
     * Return whether there is an eof before the next token.
     *
     * @return Whether there is an eof before the next token
     */
    public boolean eof ()
    {
 try
 {
     char ch = readACharacterThrowsEOF ();
     pushACharacter (ch);
 }
 catch (EOFException e)
 {
     return (true);
 }
 return (false);
    } // boolean eof (void)


    /**
     * Place the character in the unread position.  The next time a character
     * is read, this one will be returned.
     *
     * @param ch - The character to be pushed into the unread position.
     */
    protected void pushACharacter (char ch)
    {
 ungotChar = ch;
    } // pushACharacter (char)


    /**
     * Read a single character from the file. If we reach EOF before
     * reading the character, display an error message and exit.
     *
     * @return The character read from the file.
     */
    protected char readACharacter ()
    {
 try
 {
     return (readACharacterThrowsEOF ());
 }
 catch (EOFException e)
 {
     new FatalError ("Attempt to read past end of file on " +
      fileName);
     // Never reaches here.
 }

 return (0);
    } // char readACharacter (void)


    /**
     * Read a single character from the file.
     *
     * @exception EOFException - Thrown if attempt to read past EOF.
     * @return The character read from the file.
     */
    protected char readACharacterThrowsEOF () throws EOFException
    {
 // Check if stream already closed.
 if (closed)
 {
     new FatalError ("Read failed: \"" + fileName + "\" is already closed.");
     // Never reaches here.
 }

 if (ungotChar != EMPTY)
 {
     char ch = (char) ungotChar;
     ungotChar = EMPTY;
     return (ch);
 }
 if (lineBufferPtr < lineBuffer.length ())
 {
     return (lineBuffer.charAt (lineBufferPtr++));
 }

 readALineFromFile ();
 return (lineBuffer.charAt (lineBufferPtr++));
    } // char readACharacterThrowsEOF (void)


    /**
     * Reads a line from the file.
     *
     * @exception EOFException - Thrown if attempt to read past EOF.
     */
    protected void readALineFromFile () throws EOFException
    {
 try
 {
     if (useStandardIO)
     {
  if (eofFlag)
  {
      throw new EOFException ();
  }

  lineBuffer = f.readLine ();
  if (lineBuffer != null)
  {
      if (lineBuffer.indexOf ('\032') != -1)
      {
   lineBuffer = lineBuffer.substring (0,
    lineBuffer.indexOf ('\032'));
   if (lineBuffer.length () == 0)
   {
       lineBuffer = null;
   }
   eofFlag = true;
      }
      else if (lineBuffer.indexOf ('\004') != -1)
      {
   lineBuffer = lineBuffer.substring (0,
    lineBuffer.indexOf ('\004'));
   if (lineBuffer.length () == 0)
   {
       lineBuffer = null;
   }
   eofFlag = true;
      }
  }
     }
     else
     {
  lineBuffer = f.readLine ();
     }
     if (lineBuffer == null)
     {
  throw (new EOFException ());
     }
     lineBuffer = lineBuffer + "\n";
     lineBufferPtr = 0;
 }
 catch (IOException e)
 {
     if (e instanceof EOFException)
     {
  throw (new EOFException ());
     }
     else
     {
  new FatalError ("Read on \"" + fileName + "\" failed: " + e);
     }
 }
    } // readLineFromFile (void)


    /**
     * Reads in input from the keyboard buffer until it hits a
     * whitespace, which indicates the end of a token.
     */
    protected String readAToken ()
    {
 char ch;

 StringBuffer sb = new StringBuffer ();

 // Skip white space
 do
 {
     ch = readACharacter ();
 }
 while ((ch == ' ') || (ch == '\n') || (ch == '\t'));

 if (ch == '"')
 {
     // Read until close quote
     ch = readACharacter ();
     while (ch != '"')
     {
  sb.append (ch);
  ;
  ch = readACharacter ();
  if (ch == '\n')
  {
      new FatalError (
       "No terminating quote for quoted string in \"" +
       fileName + "\"");
      // Never reaches here.
  }
     }

     // Read the character following the close quote.
     ch = readACharacter ();
 }
 else
 {
     do
     {
  sb.append (ch);
  ;
  ch = readACharacter ();
     }
     while ((ch != ' ') && (ch != '\n') && (ch != '\t'));
 }

 // Lastly, skip any whitespace until the end of line
 while ((ch == ' ') || (ch == '\t'))
 {
     ch = readACharacter ();
 }

 if (ch != '\n')
 {
     pushACharacter (ch);
 }

 return (new String (sb));
    } // String readAToken (void)


    /**
     * Read a boolean from the file.
     * The actual text in the file must be either "true" or "false"
     * although case is irrelvant.
     *
     * @return The boolean value read from the file.
     */
    public boolean readBoolean ()
    {
 String s = readAToken ().toLowerCase ();

 if (s.equals ("true"))
 {
     return (true);
 }
 else if (s.equals ("false"))
 {
     return (false);
 }
 else
 {
     new FatalError ("Unable to convert \"" + s + "\" to a boolean");
     // Never reaches here
 }
 return (false);
    } // boolean readBoolean (void)


    /**
     * Read an 8-bit integer (a "byte") from the file.
     * The actual text in the file must be a number from -128 to 127.
     *
     * @return The byte value read from the file.
     */
    public byte readByte ()
    {
 String s = readAToken ();

 try
 {
     return (Byte.parseByte (s));
 }
 catch (NumberFormatException e)
 {
     new FatalError ("Unable to convert \"" + s + "\" to a byte");
     // Never reaches here
 }
 return (0);
    } // byte readByte (void)


    /**
     * Read a single character from the file.  Note that this discards any
     * whitespace.  If you want to get every character on the line, use
     * the readLine () method.
     *
     * @return The character read from the file.
     */
    public char readChar ()
    {
 char ch, result;

 // Skip white space before the character.
 do
 {
     ch = readACharacter ();
 }
 while ((ch == ' ') || (ch == '\n') || (ch == '\t'));

 // The non-whitespace character read is the one to return.
 result = ch;

 // Skip whitespace following the character until end of line.
 do
 {
     ch = readACharacter ();
 }
 while ((ch == ' ') || (ch == '\t'));

 if (ch != '\n')
 {
     pushACharacter (ch);
 }

 // Return the character read.
 return (result);
    } // char readChar (void)


    /**
     * Read a double precision floating point number (a "double") from
     * the file.
     *
     * @return The double value read from the file.
     */
    public double readDouble ()
    {
 Double d;
 String s = readAToken ();

 try
 {
     d = Double.valueOf (s);
     return (d.doubleValue ());
 }
 catch (NumberFormatException e)
 {
     new FatalError ("Unable to convert \"" + s + "\" to a double");
     // Never reaches here
 }
 return (0.0);
    } // double readDouble (void)


    /**
     * Read a floating point number (a "float") from the file.
     *
     * @return The float value read from the file.
     */
    public float readFloat ()
    {
 Float f;
 String s = readAToken ();

 try
 {
     f = Float.valueOf (s);
     return (f.floatValue ());
 }
 catch (NumberFormatException e)
 {
     new FatalError ("Unable to convert \"" + s + "\" to a float");
     // Never reaches here
 }
 return ((float) 0.0);
    } // float readFloat (void)


    /**
     * Read a 32-bit integer (an "int") from the file.
     * The actual text in the file must be a number from
     * -2147483648 to 2147483647.
     *
     * @return The int value read from the file.
     */
    public int readInt ()
    {
 String s = readAToken ();

 try
 {
     return (Integer.parseInt (s));
 }
 catch (NumberFormatException e)
 {
     new FatalError ("Unable to convert \"" + s + "\" to a int");
     // Never reaches here
 }
 return (0);
    } // int readInt (void)


    /**
     * Read a full line of text from the file.
     *
     * @return The line of text read from the file.
     */
    public String readLine ()
    {
 char ch;                                                                                                        // The character being read in
 StringBuffer s = new StringBuffer ();           // The string typed in

 // Skip whitespace up to the first newline
 ch = readACharacter ();
 while (ch != '\n')
 {
     s.append (ch);
     ch = readACharacter ();
 }

 return (s.toString ());
    } // String readLine (void)


    /**
     * Read a 64-bit integer (a "long") from the file.
     *
     * @return The long value read from the file.
     */
    public long readLong ()
    {
 String s = readAToken ();

 try
 {
     return (Long.parseLong (s));
 }
 catch (NumberFormatException e)
 {
     new FatalError ("Unable to convert \"" + s + "\" to a long");
     // Never reaches here
 }
 return (0);
    } // long readLong (void)


    /**
     * Read a 16-bit integer (a "short") from the file.
     * The actual text in the file must be a number from -32768 to 32767.
     *
     * @return The short value read from the file.
     */
    public short readShort ()
    {
 String s = readAToken ();

 try
 {
     return (Short.parseShort (s));
 }
 catch (NumberFormatException e)
 {
     new FatalError ("Unable to convert \"" + s + "\" to a short");
     // Never reaches here
 }
 return (0);
    } // short readShort (void)


    /**
     * Read a whitespace delimited token from the file.
     *
     * @return The token read from the file.
     */
    public String readString ()
    {
 return (readAToken ());
    } // String readString (void)
} /* TextInputFile class */
