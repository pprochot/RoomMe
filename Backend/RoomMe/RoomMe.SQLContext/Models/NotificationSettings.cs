using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class NotificationSettings
    {
        public int Id { get; set; }
        public DateTime BegginingDate { get; set; }
        public int Repetitions { get; set; }
        public int FrequencyId { get; set; }
        public NotificationFrequency Frequency { get; set; }
    }
}
