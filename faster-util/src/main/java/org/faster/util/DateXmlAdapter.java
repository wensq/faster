package org.faster.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * @author sqwen
 */
public final class DateXmlAdapter extends XmlAdapter<String, Date> {

    private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String marshal(Date v) throws Exception {
        return v == null ? null : fmt.print(v.getTime());
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        return v == null || v.trim().length() == 0 ? null : fmt.parseDateTime(v.trim()).toDate();
    }

}
