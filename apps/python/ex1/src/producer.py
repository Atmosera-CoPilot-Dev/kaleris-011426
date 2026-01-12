class Producer:
    def __init__(self, queue):
        self.queue = queue

    def produce(self, item):
        self.queue.put(item)