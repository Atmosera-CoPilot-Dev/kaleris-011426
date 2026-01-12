import unittest
from queue import Queue
from src.producer import Producer
from src.consumer import Consumer

class TestProducer(unittest.TestCase):
    def setUp(self):
        self.queue = Queue()
        self.producer = Producer(self.queue)

    def test_produce(self):
        item = "test_item"
        self.producer.produce(item)
        self.assertEqual(self.queue.qsize(), 1)
        self.assertEqual(self.queue.get(), item)

if __name__ == "__main__":
    unittest.main()