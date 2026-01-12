using LoggerLib;
using NUnit.Framework;
using System.IO;

namespace LoggerLib.Tests
{
    /// <summary>
    /// Tests for Logger log file creation.
    /// </summary>
    [TestFixture]
    public class LoggerFileCreationTests
    {
        private const string LogFileName = "log.txt";

        [SetUp]
        public void SetUp()
        {
            // Delete log file before each test to ensure clean state
            if (File.Exists(LogFileName))
            {
                File.Delete(LogFileName);
            }
        }

        /// <summary>
        /// Verifies that the log file is created when logging a message.
        /// NOTE: Uses naive logger (no thread safety, no disposal) on purpose.
        /// </summary>
        [Test]
        public void LogFile_IsCreated_WhenLoggingMessage()
        {
            // Arrange
            const string message = "Test message";
            // Precondition sanity check (SetUp already deleted any prior file)
            Assert.That(File.Exists(LogFileName), Is.False);

            // Act
            Logger.Instance.Log(message);

            // Assert
            Assert.That(File.Exists(LogFileName), Is.True);
        }
    }
}
