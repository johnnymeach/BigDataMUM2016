package cs.mum.edu.util

/**
  * Created by Sokly on 6/10/16.
  */
case class ApacheAccessLog(ipAddress: String, clientIdentd: String,
                           userId: String, dateTime: String, method: String,
                           endpoint: String, protocol: String,
                           responseCode: Int, contentSize: Long) {

}

object ApacheAccessLog {

  val PATTERN = """^(\S+) (\S+) (\S+) \[([\w:/]+\s[+\-]\d{4})\] "(\S+) (\S+) (\S+)" (\d{3}) (\S+)""".r

  def parseLogLine(log: String): ApacheAccessLog = {
    val res = PATTERN.findFirstMatchIn(log)
    if (res.isEmpty) {
      throw new RuntimeException("Cannot parse log line: " + log)
    }
    val m = res.get
    // replace content size "-" to 0
    val content_size : String =m.group(9).replaceAll("[^0-9]+","0")
    ApacheAccessLog(m.group(1), m.group(2), m.group(3), m.group(4),
      m.group(5), m.group(6), m.group(7), m.group(8).toInt, content_size.toLong)
  }
}