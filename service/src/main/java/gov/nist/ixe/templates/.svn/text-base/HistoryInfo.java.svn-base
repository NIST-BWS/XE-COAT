package gov.nist.ixe.templates;

import java.util.Date;

public class HistoryInfo {
	
	public HistoryInfo(long timestampInMs, Long sizeInBytes) {
		this.timestampInMs = timestampInMs;
		this.sizeInBytes = sizeInBytes;
	}
	
	public Long getTimestampInMs() {
		return this.timestampInMs;
	}
	
	public Date getTimestampAsDate() {
		return new Date(this.timestampInMs);
	}
	
	public long getSizeInBytes() {
		return this.sizeInBytes;
	}
	
	private long timestampInMs;
	private long sizeInBytes;
}