from __future__ import division
import storm

class RunningAverageBolt(storm.BasicBolt):

    running_averages = {}

    def process(self, tup):

        sensor_id, temperature = tup.values
        new_ave = temperature
        count = 1

        # recompute running average
        if sensor_id in self.running_averages:
            ave, count = self.running_averages[sensor_id]
            new_ave = (ave * count + temperature) / (count + 1)
            count = count + 1

        # store the running average
        self.running_averages[sensor_id] = [new_ave, count]

        storm.log("For sensor %s the new average is %f recorded over %d readings" % (sensor_id, new_ave, count))

        storm.emit([sensor_id, new_ave], anchors=[tup])

RunningAverageBolt().run()
