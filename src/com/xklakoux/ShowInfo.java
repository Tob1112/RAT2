package com.xklakoux;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;


public class ShowInfo extends Activity{

	String masks ="In the simplest arrangement all routers within a single AS and participating in BGP routing must be configured in a full mesh: each router must be configured as peer to every other router. This causes scaling problems, since the number of required connections grows quadratically with the number of routers involved. To alleviate the problem, BGP implements two options: route reflectors (RFC 4456) and confederations (RFC 5065). The following discussion of basic UPDATE processing assumes a full IBGP mesh."+
	"<br><b>Basic update processing</b><br>"+
	
	"A given BGP router may accept NLRI UPDATEs from multiple neighbors and advertise NLRI (Network Layer Reach-ability Information) to the same, or a different set, of neighbors. Conceptually, BGP maintains its own \"master\" routing table, called the Loc-RIB (Local Routing Information Base), separate from the main routing table of the router. For each neighbor, the BGP process maintains a conceptual Adj-RIB-In (Adjacent Routing Information Base, Incoming) containing the NLRI received from the neighbor, and a conceptual Adj-RIB-Out (Outgoing) for NLRI to be sent to the neighbor."+
	"Conceptual, in the preceding paragraph, means that the physical storage and structure of these various tables are decided by the implementer of the BGP code. Their structure is not visible to other BGP routers, although they usually can be interrogated with management commands on the local router. It is quite common, for example, to store the two Adj-RIBs and the Loc-RIB together in the same data structure, with additional information attached to the RIB entries. The additional information tells the BGP process such things as whether individual entries belong in the Adj-RIBs for specific neighbors, whether the per-neighbor route selection process made received policies eligible for the Loc-RIB, and whether Loc-RIB entries are eligible to be submitted to the local router's routing table management process."+
	"By eligible to be submitted, BGP will submit the routes that it considers best to the main routing table process. Depending on the implementation of that process, the BGP route is not necessarily selected. For example, a directly connected prefix, learned from the router's own hardware, is usually most preferred. As long as that directly connected route's interface is active, the BGP route to the destination will not be put into the routing table. Once the interface goes down, and there are no more preferred routes, the Loc-RIB route would be installed in the main routing table. Until recently, it was a common mistake to say BGP carries policies. BGP actually carried the information with which rules inside BGP-speaking routers could make policy decisions. Some of the information carried that is explicitly intended to be used in policy decisions are communities and multi-exit discriminators (MED)."+
	"<br><b>Route selection</b><br>"+
	"The BGP standard specifies a number of decision factors, more than are used by any other common routing process, for selecting NLRI (Network Layer Reach-ability Information) to go into the Loc-RIB (Routing Information Base). The first decision point for evaluating NLRI is that its next-hop attribute must be reachable (or resolvable). Another way of saying the next-hop must be reachable is that there must be an active route, already in the main routing table of the router, to the prefix in which the next-hop address is located."+
	"Next, for each neighbor, the BGP process applies various standard and implementation-dependent criteria to decide which routes conceptually should go into the Adj-RIB-In. The neighbor could send several possible routes to a destination, but the first level of preference is at the neighbor level. Only one route to each destination will be installed in the conceptual Adj-RIB-In. This process will also delete, from the Adj-RIB-In, any routes that are withdrawn by the neighbor."+
	"Whenever a conceptual Adj-RIB-In changes, the main BGP process decides if any of the neighbor's new routes are preferred to routes already in the Loc-RIB. If so, it replaces them. If a given route is withdrawn by a neighbor, and there is no other route to that destination, the route is removed from the Loc-RIB, and no longer sent, by BGP, to the main routing table manager. If the router does not have a route to that destination from any non-BGP source, the withdrawn route will be removed from the main routing table."+
	"<br><b>Per-neighbor decisions</b><br>"+
	"After verifying that the next hop is reachable, if the route comes from an internal (i.e. IBGP) peer, the first rule to apply according to the standard is to examine the LOCAL_PREFERENCE attribute. If there are several IBGP routes from the neighbor, the one with the highest LOCAL_PREFERENCE is selected unless there are several routes with the same LOCAL_PREFERENCE. In the latter case the route selection process moves to the next tie breaker. While LOCAL_PREFERENCE is the first rule in the standard, once reach-ability of the NEXT_HOP is verified, Cisco and several other vendors first consider a decision factor called WEIGHT which is local to the router (i.e. not transmitted by BGP). The route with the highest WEIGHT is preferred."+
	"The LOCAL_PREFERENCE, WEIGHT, and other criteria can be manipulated by local configuration and software capabilities. Such manipulation is outside the scope of the standard but is commonly used. For example the COMMUNITY attribute (see below) is not directly used by the BGP selection process. The BGP neighbor process however can have a rule to set LOCAL_PREFERENCE or another factor based on a manually programmed rule to set the attribute if the COMMUNITY value matches some pattern matching criterion. If the route was learned from an external peer the per-neighbor BGP process computes a LOCAL_PREFERENCE value from local policy rules and then compares the LOCAL_PREFERENCE of all routes from the neighbor."+
	"At the per-neighbor level - ignoring implementation-specific policy modifiers - the order of tie breaking rules is:"+
	"Prefer the route with the shortest AS_PATH. An AS_PATH is the set of AS numbers that must be traversed to reach the advertised destination. AS1-AS2-AS3 is shorter than AS4-AS5-AS6-AS7."+
	"Prefer routes with the lowest value of their ORIGIN attribute."+
	"Prefer routes with the lowest MULTI_EXIT_DISC (multi-exit discriminator or MED) value."+
	"Before the most recent edition of the BGP standard, if an UPDATE had no MULTI_EXIT_DISC value, several implementations"+
	"<br><b>Decision factors at the Loc-RIB level</b>"+
	"Once candidate routes are received from neighbors, the Loc-RIB software applies additional tie-breakers to routes to the same destination."+
	"If at least one route was learned from an external neighbor (i.e., the route was learned from EBGP), drop all routes learned from IBGP."+
	"Prefer the route with the lowest interior cost to the NEXT_HOP, according to the main Routing Table. If two neighbors advertised the same route, but one neighbor is reachable via a low-bitrate link and the other by a high-bitrate link, and the interior routing protocol calculates lowest cost based on highest bitrate, the route through the high-bitrate link would be preferred and other routes dropped."+
	"If there is more than one route still tied at this point, several BGP implementations offer a configurable option to load-share among the routes, accepting all (or all up to some number)."+
	"Prefer the route learned from the BGP speaker with the numerically lowest BGP identifier"+
	"Prefer the route learned from the BGP speaker with the lowest peer IP address"+
	"<br><b>Communities</b><br>"+
	"BGP communities are attribute tags that can be applied to incoming or outgoing prefixes to achieve some common goal (RFC 1997). While it is common to say that BGP allows an administrator to set policies on how prefixes are handled by ISPs, this is generally not possible, strictly speaking. For instance, BGP natively has no concept to allow one AS to tell another AS to restrict advertisement of a prefix to only North American peering customers. Instead, an ISP generally publishes a list of well-known or proprietary communities with a description for each one, which essentially becomes an agreement of how prefixes are to be treated. Examples of common communities include local preference adjustments, geographic or peer type restrictions, DoS avoidance (black holing), and AS prepending options. An ISP might state that any routes received from customers with community XXX:500 will be advertised to all peers (default) while community XXX:501 will restrict advertisement to North America. The customer simply adjusts their configuration to include the correct community(ies) for each route, and the ISP is responsible for controlling who the prefix is advertised to. The end user has no technical ability to enforce correct actions being taken by the ISP, though problems in this area are generally rare and accidental."+
	"It is a common tactic for end customers to use BGP communities (usually ASN:70,80,90,100) to control the local preference the ISP assigns to advertised routes instead of using MED (the effect is similar). It should also be noted that the community attribute is transitive, but communities applied by the customer very rarely become propagated outside the next-hop AS."+
	"<br><b>Extended communities</b><br>"+
	"The BGP Extended Community Attribute was added in 2006 in order to extend the range of such attributes and to provide a community attribute structuring by means of a type field. The extended format consists of one or two octets for the type field followed by seven or six octets for the respective community attribute content. The definition of this Extended Community Attribute is documented in RFC 4360. The IANA administers the registry for BGP Extended Communities Types.[5] The Extended Communities Attribute itself is a transitive optional BGP attribute. However, a bit in the type field within the attribute decides whether the encoded extended community is of a transitive or non-transitive nature. The IANA registry therefore provides different number ranges for the attribute types. Due to the extended attribute range, its usage can be manifold. RFC 4360 exemplarly defines the \"Two-Octet AS Specific Extended Community\", the \"IPv4 Address Specific Extended Community\", the \"Opaque Extended Community\", the \"Route Target Community\" and the \"Route Origin Community\". A number of BGP QoS drafts[6] also use this Extended Community Attribute structure for inter-domain QoS signalling."+
	"<br><b>Uses of multi-exit discriminators</b><br>"+
	"MEDs, defined in the main BGP standard, were originally intended to show to another neighbor AS the advertising AS's preference as to which of several links are preferred for inbound traffic. Another application of MEDs is to advertise the value, typically based on delay, of multiple AS that have presence at an IXP, that they impose to send traffic to some destination.";
	String aaa = "<b>Cidr and dot decimal masks</b><br>" +
	"/24	255.255.255.0		1	254	254<br>"+
	"/25	255.255.255.128	2	126	252<br>"+
	"/26	255.255.255.192	4	62	248<br>"+
	"/27	255.255.255.224	8	30	240<br>"+
	"/28	255.255.255.240	16	14	224<br>"+
	"/29	255.255.255.248	32	6	192<br>"+
	"/30	255.255.255.252	64	2	128<br>"+
	"/31	255.255.255.254	128	2*	256<br>";
	String about = new String();
	TextView content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showinfo);
		content = (TextView) findViewById(R.id.tvContent);
		about = getIntent().getExtras().get("about").toString();
		
		content.setText(Html.fromHtml(masks));
//		Toast.makeText(getApplicationContext(), about, Toast.LENGTH_SHORT).show();

	}
}


