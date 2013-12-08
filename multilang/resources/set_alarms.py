import storm

class SetAlarmsBolt(storm.BasicBolt):
    def process(self, tup):
        sensor_id, temperature = tup.values
        if temperature > 22:
            storm.log("ALARM STATE!!! for sensor: %s current temperature is: %f" % (sensor_id, temperature))
        storm.emit([sensor_id, temperature], anchors=[tup])

SetAlarmsBolt().run()
