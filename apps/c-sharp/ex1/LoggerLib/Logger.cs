using System;
using System.IO;

namespace LoggerLib
{

  public sealed class Logger
  {
    private static Logger _instance;
    private const string LogFileName = "log.txt";

    public static Logger Instance => _instance ??= new Logger();

    private Logger() { }

    public void Log(string message)
    {
      // Naive append: opens and closes file every call; no error handling.
      File.AppendAllText(LogFileName, message + Environment.NewLine);
    }
  }
}
