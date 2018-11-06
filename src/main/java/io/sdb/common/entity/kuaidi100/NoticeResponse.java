package io.sdb.common.entity.kuaidi100;

import com.thoughtworks.xstream.XStream;

public class NoticeResponse {
	private static XStream xstream;

	private Boolean result;
	private String returnCode;
	private String message;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private static XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream();
			xstream.autodetectAnnotations(true);
			xstream.alias("pushResponse", NoticeResponse.class);
		}
		return xstream;
	}

	public String toXml(){
		return "<?xml version='1.0' encoding='UTF-8'?>\r\n" + getXStream().toXML(this);
	}

	public static NoticeResponse fromXml(String sXml){
		return (NoticeResponse)getXStream().fromXML(sXml);
	}


	public static void main(String[] args){
		NoticeResponse req = new NoticeResponse();
		req.setMessage("成功");
		req.setResult(true);
		req.setReturnCode("200");
		System.out.print(req.toXml());
	}
}
