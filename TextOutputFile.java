package hsa2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * The TextOutputFile writes text to a file. The class is a superset of
 * PrintWriter adding formatted output and integrated error checking.
 * <p>
 * Full documentation for the classes in the hsa package available at:
 * <br>
 *                      http://www.holtsoft.com/java/hsa_package.html
 * <p>
 * @author Tom West
 * @version 2.0 99/02/01
 */
public class TextOutputFile
{
	/**
	 * The file name.
	 */
	protected String fileName;

	/**
	 * Streams used for the file
	 */
	protected PrintWriter f = null;

	/**
	 * Set if the file has been closed.
	 */
	protected boolean closed = false;

	/**
	 * Indicator whether to write to standard output or a file.
	 */
	protected boolean useStandardIO = false;

	/**
	 * The line separator in files
	 */
	protected String lineSeparator = System.getProperty ("line.separator");


	/**
	 * Contructor - TextOutputFile to write to standard output.
	 */
	public TextOutputFile ()
	{
		this ("Standard output");
	} // Constructor - TextOutputFile (void)


	/**
	 * Contructor - TextOutputFile to write to a File.
	 *
	 * @param file - File to be opened.
	 */
	public TextOutputFile (File file)
	{
		this (file, false);
	} // Constructor - TextOutputFile (File)


	/**
	 * Contructor - TextOutputFile to write or append to a File.
	 *
	 * @param file - File to be opened.
	 * @param append - True if file is to be appended to.
	 */
	public TextOutputFile (File file, boolean append)
	{
		try
		{
			if (append)
			{
				f = new PrintWriter (new FileWriter (file.getName (), append));
			}
			else
			{
				f = new PrintWriter (new FileWriter (file));
			}
		}
		catch (IOException e)
		{
			new FatalError ("Unable to open file \"" + fileName + "\"");
			// Never reaches here
		}
		this.fileName = file.getName ();
	} // Constructor - TextOutputFile (File, boolean)


	/**
	 * Contructor - TextFile to write to file with specified name.
	 *
	 * @param fileName - Name of the file to be opened.
	 */
	public TextOutputFile (String fileName)
	{
		this (fileName, false);
	} // Constructor - TextOutputFile (String)


	/**
	 * Contructor - TextFile to write or append to file with specified name.
	 *
	 * @param fileName - Name of the file to be opened.
	 * @param append - True if file is to be appended to.
	 */
	public TextOutputFile (String fileName, boolean append)
	{
		if (fileName.equalsIgnoreCase ("standard output") ||
				fileName.equalsIgnoreCase ("screen") ||
				fileName.equalsIgnoreCase ("stdout"))
		{
			this.fileName = "Standard output";
			useStandardIO = true;
		}
		else
		{
			try
			{
				f = new PrintWriter (new FileWriter (fileName, append));
			}
			catch (IOException e)
			{
				new FatalError ("Unable to open file \"" + fileName + "\"");
				// Never reaches here
			}
			this.fileName = fileName;
		}
	} // Constructor - TextOutputFile (String, boolean)


	/**
	 * Close the file to further writing.
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
			f.close ();
			if (f.checkError ())
			{
				new FatalError ("Close failed: Unable to close \"" + fileName + "\"");
				// Never reaches here.
			}
			f = null;
		}

		closed = true;
	} // close (void)


	public boolean isStandardOut ()
	{
		return (useStandardIO);
	}


	/**
	 * Write the text representation of an 8-bit integer (a "byte") to
	 * the file.
	 *
	 * @param number The number to be written to the file.
	 */
	public void print (byte number)
	{
		print ((int) number);
	} // print (byte)


	/**
	 * Write the text representation of an 8-bit integer (a "byte")
	 * to the file with a specified field size.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (byte number, int fieldSize)
	{
		print ((int) number, fieldSize);
	} // print (byte, int)


	/**
	 * Write a character to the file.
	 *
	 * @param ch The character to be written to the file.
	 */
	public void print (char ch)
	{
		print (String.valueOf (ch));
	} // print (char)


	/**
	 * Write a character to the file with a specified field size..
	 *
	 * @param ch The character to be written to the file.
	 * @param fieldSize The field width that the character is to be written in.
	 */
	public void print (char ch, int fieldSize)
	{
		String charStr = String.valueOf (ch);
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - charStr.length () ; cnt++)
		{
			padding.append (' ');
		}
		print (charStr + padding);
	} // print (char, int)


	/**
	 * Write a double precision floating point number (a "double") to
	 * the file.
	 *
	 * @param number The number to be written to the file.
	 */
	public void print (double number)
	{
		print (String.valueOf (number));
	} // print (double)


	/**
	 * Write a double precision floating point number (a "double") to
	 * the file with a specified field size.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (double number, int fieldSize)
	{
		double posValue = Math.abs (number);
		int placesRemaining = fieldSize;
		String format = null, numStr;
		StringBuffer padding = new StringBuffer ();

		if (number < 0)
			placesRemaining--;                 // Space for the minus sign
		if (posValue < 10.0)
			format = "0";
		else if (posValue < 100.0)
			format = "00";
		else if (posValue < 1000.0)
			format = "000";
		else if (posValue < 10000.0)
			format = "0000";
		else if (posValue < 100000.0)
			format = "00000";
		else if (posValue < 1000000.0)
			format = "000000";
		else if (posValue < 10000000.0)
			format = "0000000";
		else if (posValue < 100000000.0)
			format = "00000000";

		if (format == null)
		{
			// We're using scientific notation
			numStr = String.valueOf (number);
		}
		else
		{
			// Add a decimal point, if there's room
			placesRemaining -= format.length ();
			if (placesRemaining > 0)
			{
				format = format + ".";
				placesRemaining--;
			}

			// For any addition room, add decimal places
			for (int cnt = 0 ; cnt < placesRemaining ; cnt++)
			{
				format = format + "#";
			}

			// Convert the number
			NumberFormat form = new DecimalFormat (format);
			numStr = form.format (number);
		}

		// If the number is not long enough, pad with spaces
		for (int cnt = 0 ; cnt < fieldSize - numStr.length () ; cnt++)
		{
			padding.append (' ');
		}
		print (padding + numStr);
	} // print (double, int)


	/**
	 * Write a double precision floating point number (a "double") to
	 * the file with a specified field size and a specified number of
	 * decimal places.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 * @param decimalPlaces The number of decimal places of the number
	 *    to be displayed.
	 */
	public void print (double number, int fieldSize, int decimalPlaces)
	{
		double posValue = Math.abs (number);
		int placesRemaining = fieldSize;
		String format = null, numStr;
		StringBuffer padding = new StringBuffer ();

		if (number < 0)
			placesRemaining--;                 // Space for the minus sign
		if (posValue < 10.0)
			format = "0";
		else if (posValue < 100.0)
			format = "00";
		else if (posValue < 1000.0)
			format = "000";
		else if (posValue < 10000.0)
			format = "0000";
		else if (posValue < 100000.0)
			format = "00000";
		else if (posValue < 1000000.0)
			format = "000000";
		else if (posValue < 10000000.0)
			format = "0000000";
		else if (posValue < 100000000.0)
			format = "00000000";

		if (Math.abs (number) >= 100000000.0)
		{
			// We're using scientific notation
			numStr = String.valueOf (number);
		}
		else
		{
			format = "0.";

			// For any addition room, add decimal places
			for (int cnt = 0 ; cnt < decimalPlaces ; cnt++)
			{
				format = format + "0";
			}

			// Convert the number
			NumberFormat form = new DecimalFormat (format);
			form.setMinimumIntegerDigits (1);
			numStr = form.format (number);
		}

		// If the number is not long enough, pad with spaces
		for (int cnt = 0 ; cnt < fieldSize - numStr.length () ; cnt++)
		{
			padding.append (' ');
		}
		print (padding + numStr);
	} // print (double, int, int)


	/**
	 * Write a floating point number (a "float") to the file.
	 *
	 * @param number The number to be written to the file.
	 */
	public void print (float number)
	{
		print (String.valueOf (number));
	} // print (float)


	/**
	 * Write a floating point number (a "float") to
	 * the file with a specified field size.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (float number, int fieldSize)
	{
		print ((double) number, fieldSize);
	} // print (float, int)


	/**
	 * Write a floating point number (a "double") to the file with a
	 * specified field size and a specified number of decimal places.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 * @param decimalPlaces The number of decimal places of the number
	 *    to be displayed.
	 */
	public void print (float number, int fieldSize, int decimalPlaces)
	{
		print ((double) number, fieldSize, decimalPlaces);
	} // print (float, int, int)


	/**
	 * Write the text representation of an 32-bit integer (an "int") to
	 * the file.
	 *
	 * @param number The number to be written to the file.
	 */
	public void print (int number)
	{
		print (String.valueOf (number));
	} // print (int)


	/**
	 * Write the text representation of an 32-bit integer (an "int")
	 * to the file with a specified field size.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (int number, int fieldSize)
	{
		String numStr = String.valueOf (number);
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - numStr.length () ; cnt++)
		{
			padding.append (' ');
		}
		print (padding + numStr);
	} // print (int, int)


	/**
	 * Write the text representation of an 64-bit integer (a "long") to
	 * the file.
	 *
	 * @param number The number to be written to the file.
	 */
	public void print (long number)
	{
		print (String.valueOf (number));
	} // print (long)


	/**
	 * Write the text representation of an 64-bit integer (a "long")
	 * to the file with a specified field size.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (long number, int fieldSize)
	{
		String numStr = String.valueOf (number);
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - numStr.length () ; cnt++)
		{
			padding.append (' ');
		}
		print (padding + numStr);
	} // print (long, int)


	/**
	 * Write a string to the file.
	 *
	 * @param text The string to be written to the file.
	 */
	public void print (String text)
	{
		// Convert newline to line separators, if necessary.
		if (!useStandardIO)
		{
			int newLinePosition = 0;
			while (text.indexOf ('\n', newLinePosition) != -1)
			{
				newLinePosition = text.indexOf ('\n', newLinePosition);
				text = text.substring (0, newLinePosition) + lineSeparator +
						text.substring (newLinePosition + 1);
				newLinePosition += lineSeparator.length ();
			}
		}

		// Check if stream already closed.
		if (closed)
		{
			new FatalError ("Write failed: \"" + fileName + "\" is already closed.");
			// Never reaches here.
		}

		if (useStandardIO)
		{
			// Sending output to stdout.
			System.out.print (text);
			System.out.flush ();

			if (System.out.checkError ())
			{
				new FatalError ("Write failed: Unable to write to \"" + fileName + "\"");
				// Never reaches here.
			}
		}
		else
		{
			// Sending output to PrintWriter
			f.print (text);

			if (f.checkError ())
			{
				new FatalError ("Write failed: Unable to write to \"" + fileName + "\"");
				// Never reaches here.
			}
		}
	} // print (String)


	/**
	 * Write a string to the file with a specified field size..
	 *
	 * @param text The string to be written to the file.
	 * @param fieldSize The field width that the string is to be written in.
	 */
	public void print (String text, int fieldSize)
	{
		StringBuffer padding = new StringBuffer ();
		for (int cnt = 0 ; cnt < Math.abs (fieldSize) - text.length () ; cnt++)
		{
			padding.append (' ');
		}
		if (fieldSize >= 0)
		{
			print (text + padding);
		}
		else
		{
			print (padding + text);
		}
	} // print (String, int)


	/**
	 * Write the text representation of an 16-bit integer (a "short") to
	 * the file.
	 *
	 * @param number The number to be written to the file.
	 */
	public void print (short number)
	{
		print ((int) number);
	} // print (short)


	/**
	 * Write the text representation of an 16-bit integer (a "short")
	 * to the file with a specified field size.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void print (short number, int fieldSize)
	{
		print ((int) number, fieldSize);
	} // print (short, int)


	/**
	 * Write the text representation of a boolean to the file.
	 *
	 * @param value The boolean to be written to the file.
	 */
	public void print (boolean value)
	{
		print (String.valueOf (value));
	} // print (boolean)


	/**
	 * Write the text representation of a boolean to the file with a
	 * specified field size.
	 *
	 * @param value The boolean to be written to the file.
	 * @param fieldSize The field width that the boolean is to be written in.
	 */
	public void print (boolean value, int fieldSize)
	{
		String boolStr = String.valueOf (value);
		StringBuffer padding = new StringBuffer ();

		for (int cnt = 0 ; cnt < fieldSize - boolStr.length () ; cnt++)
		{
			padding.append (' ');
		}
		print (boolStr + padding);
	} // print (boolean, int)


	/**
	 * Write a newline to the file.
	 */
	public void println ()
	{
		print ("\n");
	} // println (void)


	/**
	 * Write the text representation of an 8-bit integer (a "byte") to
	 * the file followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 */
	public void println (byte number)
	{
		print (number);
		print ("\n");
	} // println (byte)


	/**
	 * Write the text representation of an 8-bit integer (a "byte")
	 * to the file with a specified field size followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (byte number, int fieldSize)
	{
		print (number, fieldSize);
		print ("\n");
	} // println (byte, int)


	/**
	 * Write a character to the file followed by a newline.
	 *
	 * @param ch The character to be written to the file.
	 */
	public void println (char ch)
	{
		print (ch);
		print ("\n");
	} // println (char)


	/**
	 * Write a character to the file with a specified field size..
	 *
	 * @param ch The character to be written to the file.
	 * @param fieldSize The field width that the character is to be written in.
	 */
	public void println (char ch, int fieldSize)
	{
		print (ch, fieldSize);
		print ("\n");
	} // print (char, int)


	/**
	 * Write a double precision floating point number (a "double") to
	 * the file followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 */
	public void println (double number)
	{
		print (number);
		print ("\n");
	} // println (double)


	/**
	 * Write a double precision floating point number (a "double") to
	 * the file with a specified field size followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (double number, int fieldSize)
	{
		print (number, fieldSize);
		print ("\n");
	} // println (double, int)


	/**
	 * Write a double precision floating point number (a "double") to
	 * the file with a specified field size and a specified number of
	 * decimal places followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 * @param decimalPlaces The number of decimal places of the number
	 *    to be displayed.
	 */
	public void println (double number, int fieldSize, int decimalPlaces)
	{
		print (number, fieldSize, decimalPlaces);
		print ("\n");
	} // println (double, int, int)


	/**
	 * Write a floating point number (a "float") to the file followed by
	 * a newline.
	 *
	 * @param number The number to be written to the file.
	 */
	public void println (float number)
	{
		print (number);
		print ("\n");
	} // println (float)


	/**
	 * Write a floating point number (a "float") to the file with a
	 * specified field size followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (float number, int fieldSize)
	{
		print (number, fieldSize);
		print ("\n");
	} // println (float, int)


	/**
	 * Write a floating point number (a "double") to the file with a
	 * specified field size and a specified number of decimal places
	 * followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 * @param decimalPlaces The number of decimal places of the number
	 *    to be displayed.
	 */
	public void println (float number, int fieldSize, int decimalPlaces)
	{
		print (number, fieldSize, decimalPlaces);
		print ("\n");
	} // println (float, int, int)


	/**
	 * Write the text representation of an 32-bit integer (an "int") to
	 * the file followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 */
	public void println (int number)
	{
		print (number);
		print ("\n");
	} // println (int)


	/**
	 * Write the text representation of an 32-bit integer (an "int")
	 * to the file with a specified field size followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (int number, int fieldSize)
	{
		print (number, fieldSize);
		print ("\n");
	} // println (int, int)


	/**
	 * Write the text representation of an 64-bit integer (a "long") to
	 * the file followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 */
	public void println (long number)
	{
		print (number);
		print ("\n");
	} // println (long)


	/**
	 * Write the text representation of an 64-bit integer (a "long")
	 * to the file with a specified field size followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (long number, int fieldSize)
	{
		print (number, fieldSize);
		print ("\n");
	} // println (long, int)


	/**
	 * Write a string to the file followed by a newline.
	 *
	 * @param text The string to be written to the file.
	 */
	public void println (String text)
	{
		print (text);
		print ("\n");
	} // print (String)


	/**
	 * Write a string to the file with a specified field size followed by
	 * a newline.
	 *
	 * @param text The string to be written to the file.
	 * @param fieldSize The field width that the string is to be written in.
	 */
	public void println (String text, int fieldSize)
	{
		print (text, fieldSize);
		print ("\n");
	} // println (String, int)


	/**
	 * Write the text representation of an 16-bit integer (a "short") to
	 * the file followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 */
	public void println (short number)
	{
		print (number);
		print ("\n");
	} // println (short)


	/**
	 * Write the text representation of an 16-bit integer (a "short")
	 * to the file with a specified field size followed by a newline.
	 *
	 * @param number The number to be written to the file.
	 * @param fieldSize The field width that the number is to be written in.
	 */
	public void println (short number, int fieldSize)
	{
		print (number, fieldSize);
		print ("\n");
	} // println (short, int)


	/**
	 * Write the text representation of a boolean to the file followed
	 * by a newline.
	 *
	 * @param value The boolean to be written to the file.
	 */
	public void println (boolean value)
	{
		print (value);
		print ("\n");
	} // println (boolean)


	/**
	 * Write the text representation of a boolean to the file with a
	 * specified field size followed by a newline.
	 *
	 * @param value The boolean to be written to the file.
	 * @param fieldSize The field width that the boolean is to be written in.
	 */
	public void println (boolean value, int fieldSize)
	{
		print (value, fieldSize);
		print ("\n");
	} // println (boolean, int)
} /* TextOutputFile class */
