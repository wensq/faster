/*
 * Copyright (c) 2013 @iSQWEN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.faster.orm.stats;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sqwen
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Member implements Serializable {

	private static final long serialVersionUID = -2736959355567471502L;

	@XmlAttribute
	private String type;

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String name;

	@XmlAttribute
	private int count;

	@XmlElement(name = "Member")
	private List<Member> members;

	public Member() {}

	public Member(String type, String id, String name) {
		this.type = type;
		this.id = id;
		this.name = name;
	}

	public Member(String type, String id, String name, int count) {
		this.type = type;
		this.id = id;
		this.name = name;
		this.count = count;
	}

	public void addMember(Member member) {
		if (members == null) {
			members = new LinkedList<Member>();
		}
		members.add(member);
	}

	public int sum() {
		return StatsUtils.sum(members);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

}
