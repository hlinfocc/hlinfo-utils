/**
 * 
 */
package net.hlinfo.opt;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import org.springframework.util.ObjectUtils;


public class IpUtil {
	/**
	 * IPV6 回环地址
	 */
	private static String ipv6LoopbackAddress = "0:0:0:0:0:0:0:1";
	/*
     * 获取本机所有网卡信息   得到所有IPV4信息
     * @return Inet4Address
     */
	private static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
        List<Inet4Address> addresses = new ArrayList<>(1);

        // 所有网络接口信息
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (ObjectUtils.isEmpty(networkInterfaces)) {
            return addresses;
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            //过滤点对点网卡、非活动网卡、虚拟网卡、VirtualBox虚拟网卡等并要求网卡名字是eth、en或wl开头
            if (!isValidInterface(networkInterface)) {
                continue;
            }

            // 所有网络接口的IP地址信息
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                // 判断是否是IPv4，并过滤回环地址.
                if (isValidIPV4Address(inetAddress)) {
                    addresses.add((Inet4Address) inetAddress);
                }
            }
        }
        return addresses;
    }
	/*
     * 获取本机所有网卡信息   得到所有IPV6信息
     * @return Inet4Address
     */
	private static List<Inet6Address> getLocalIpV6AddressFromNetworkInterface() throws SocketException {
        List<Inet6Address> addresses = new ArrayList<Inet6Address>();

        // 所有网络接口信息
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (ObjectUtils.isEmpty(networkInterfaces)) {
            return addresses;
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            //过滤点对点网卡、非活动网卡、虚拟网卡、VirtualBox虚拟网卡等并要求网卡名字是eth、en或wl开头
            if (!isValidInterface(networkInterface)) {
                continue;
            }

            // 所有网络接口的IP地址信息
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                // 判断是否是IPv6，并过滤回环地址.
                if (isValidIPV6Address(inetAddress)) {
                    addresses.add((Inet6Address) inetAddress);
                }
                // 获取IPv6正确的回环地址
                if(inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
            		ipv6LoopbackAddress = inetAddress.getHostAddress().split("%")[0];
                }
            }
        }
        return addresses;
    }

    /**
     * 过滤点对点网卡、非活动网卡、虚拟网卡、VirtualBox虚拟网卡等并要求网卡名字是eth、en或wl开头
     *
     * @param ni 网卡
     * @return 如果满足要求则true，否则false
     */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
    	 boolean rs = !ni.isPointToPoint() 
    			 && ni.isUp() 
    			 && !ni.isVirtual()
                 && (ni.getName().startsWith("eth") 
                		 || ni.getName().startsWith("en")
                		 || ni.getName().startsWith("wl")
                		 || ni.getName().startsWith("lo")
                && !ni.getDisplayName().contains("VirtualBox"));
        return rs;
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     * @param address InetAddress 信息
     * @return 是IPv4 返回true
     */
    private static boolean isValidIPV4Address(InetAddress address) {
        return address instanceof Inet4Address 
        		&& address.isSiteLocalAddress() 
        		&& !address.isLoopbackAddress();
    }
    
    /**
     * 判断是否是IPv6，并且过滤内网地址及过滤回环地址.
     * @param address InetAddress 信息
     * @return 是IPv6 返回true
     */
    private static boolean isValidIPV6Address(InetAddress address) {
        return address instanceof Inet6Address && !address.isLoopbackAddress();
    }

    /*
     * 通过Socket 唯一确定一个IP
     * 当有多个网卡的时候，使用这种方式一般都可以得到想要的IP。甚至不要求外网地址1.1.1.1是可连通的
     * @return Inet4Address
     */
    private static Optional<Inet4Address> getIpBySocket() throws SocketException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("1.1.1.1"), 10002);
            if (socket.getLocalAddress() instanceof Inet4Address) {
                return Optional.of((Inet4Address) socket.getLocalAddress());
            }
        } catch (UnknownHostException networkInterfaces) {
            throw new RuntimeException(networkInterfaces);
        }
        return Optional.empty();
    }
    
    /*
     * 通过Socket 唯一确定一个IP
     * 当有多个网卡的时候，使用这种方式一般都可以得到想要的IP。甚至不要求外网地址是可连通的
     * @return Inet4Address
     */
    private static Optional<Inet6Address> getIpV6BySocket() throws SocketException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("2400:3200::1"), 10002);
            if (socket.getLocalAddress() instanceof Inet6Address) {
                return Optional.of((Inet6Address) socket.getLocalAddress());
            }
        } catch (UnknownHostException networkInterfaces) {
            throw new RuntimeException(networkInterfaces);
        }
        return Optional.empty();
    }

    /*
     * 获取本地IPv4地址
     * @return Inet4Address>
     */
    private static Optional<Inet4Address> getLocalIp4Address() throws SocketException {
        final List<Inet4Address> inet4Addresses = getLocalIp4AddressFromNetworkInterface();
        if (inet4Addresses.size() != 1) {
            final Optional<Inet4Address> ipBySocketOpt = getIpBySocket();
            if (ipBySocketOpt.isPresent()) {
                return ipBySocketOpt;
            } else {
                return inet4Addresses.isEmpty() ? Optional.empty() : Optional.of(inet4Addresses.get(0));
            }
        }
        return Optional.of(inet4Addresses.get(0));
    }
    /*
     * 获取本地IPv6地址
     * @return Inet4Address>
     */
    private static Optional<Inet6Address> getLocalIpV6Addresses() throws SocketException {
        final List<Inet6Address> inet6AddressList = getLocalIpV6AddressFromNetworkInterface();
        if (inet6AddressList.size() != 1) {
            final Optional<Inet6Address> ipBySocketOpt = getIpV6BySocket();
            if (ipBySocketOpt.isPresent()) {
                return ipBySocketOpt;
            } else {
                return inet6AddressList.isEmpty() ? Optional.empty() : Optional.of(inet6AddressList.get(0));
            }
        }
        return Optional.of(inet6AddressList.get(0));
    }
    /**
     * 获取本机IPV4的IP地址
     * @return 返回IP数组，如有多个网卡在线均可获取
     */
    public static List<String> getLocalIpV4Address(){
    	List<String> list = new ArrayList<String>();
    	try {
			Optional<Inet4Address> localData = getLocalIp4Address();
			localData.ifPresent(item->{
				list.add(item.getHostAddress());
			});
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			list.add(InetAddress.getLoopbackAddress().getHostAddress());
			return list;
		}
    }
    
    /**
     * 获取本机IPV6的IP地址
     * @return 返回IP数组，如有多个网卡在线均可获取
     */
    public static List<String> getLocalIpV6Address(){
    	List<String> list = new ArrayList<String>();
    	try {
			Optional<Inet6Address> localData = getLocalIpV6Addresses();
			localData.ifPresent(item->{
				int idnetinteIdx = item.getHostAddress().indexOf("%");
				if(idnetinteIdx>0) {					
					list.add(item.getHostAddress().substring(0, idnetinteIdx));
				}
			});
			if(list.isEmpty()) {
				list.add(ipv6LoopbackAddress);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			list.add(ipv6LoopbackAddress);
			return list;
		}
    }
    
    /**
     * 获得访问者的IP地址, 反向代理过的也可以获得,针对Java EE
     *
     * @param request 请求的request对象
     * @return 来源ip
     */
    public static String getRemoteIp(javax.servlet.http.HttpServletRequest request) {
        if (request == null)
            return "0.0.0.0";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
	    				// 根据网卡取本机配置的IP
	    				InetAddress inet = null;
	    				try {
	    					inet = InetAddress.getLocalHost();
	    				} catch (UnknownHostException e) {
	    					e.printStackTrace();
	    				}
	    				ip = inet.getHostAddress();
    				}
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        if (Func.isBlank(ip))
            return "0.0.0.0";
        if (Func.isIPv4Address(ip) || Func.isIPv6Address(ip)) {
            return ip;
        }
        return "0.0.0.0";
    }
}
