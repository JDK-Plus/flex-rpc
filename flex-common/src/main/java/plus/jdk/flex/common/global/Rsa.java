package plus.jdk.flex.common.global;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 封装 RSA 加密算法
 * @author gaopengfei68
 * @date 2024-09-19 15:43
 */
@Slf4j
public class Rsa {

    /**
     * 定义用于加密和解密的字符集编码。
     */
    public static final String CHARSET = "UTF-8";
    /**
     * 定义RSA加密算法的名称。
     */
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * 存储RSA公钥，用于加密数据。
     */
    private RSAPublicKey publicKey;

    /**
     * 存储RSA私钥，用于加密和解密数据。
     */
    private RSAPrivateKey privateKey;

    /**
     * 使用指定的 RSAPublicKey 对象初始化 Rsa 对象。
     */
    public Rsa(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * 构造一个新的Rsa对象，使用指定的私钥。
     */
    public Rsa(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * 根据给定的 RSA 私钥和公钥实例化一个 Rsa 对象。
     */
    public static Rsa getRSAInstance(RSASecret rsaSecret) {
        try{
            return new Rsa(rsaSecret.getPublicKey(), rsaSecret.getPrivateKey());
        }catch (Exception e) {
            throw new FlexRpcException(e);
        }
    }

    /**
     * 使用给定的公钥和私钥初始化RSA对象。
     */
    public Rsa(String publicKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (publicKey != null) {
            this.publicKey = getPublicKey(publicKey);
        }
        if (privateKey != null) {
            this.privateKey = getPrivateKey(privateKey);
        }
    }

    /**
     * 使用给定的RSA密钥对初始化对象。
     */
    public Rsa(RsaStringKeyPair keyPair) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this(keyPair.getPublicKey(), keyPair.getPrivateKey());
    }

    /**
     * 生成指定大小的RSA密钥对并返回其Base64编码的字符串表示形式。
     */
    public static RsaStringKeyPair createKeys(int keySize) {
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        //返回一个 publicKey 经过base64encode的字符串
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        // 返回一个 privateKey 经过base64encode的字符串
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        return new RsaStringKeyPair(publicKeyStr, privateKeyStr);
    }

    /**
     * 得到公钥
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * 得到私钥
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     */
    public String publicEncrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
    }

    /**
     * 使用RSA公钥加密数据。
     */
    public byte[] publicEncrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data, publicKey.getModulus().bitLength());
    }

    /**
     * 私钥解密
     */
    public String privateDecrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
    }

    /**
     * 使用私钥解密数据。
     */
    public byte[] privateDecrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, data, privateKey.getModulus().bitLength());
    }

    /**
     * 私钥解密
     */
    public byte[] privateDecryptBytes(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength());
    }

    /**
     * 私钥加密
     */
    public String privateEncrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
    }

    /**
     * 使用私钥对数据进行加密。
     */
    public byte[] privateEncrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data, privateKey.getModulus().bitLength());
    }

    /**
     * 公钥解密
     */
    public String publicDecrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
    }

    /**
     * 使用RSA公钥解密数据。
     */
    public byte[] publicDecrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, data, publicKey.getModulus().bitLength());
    }

    /**
     * 公钥解密
     */
    public byte[] publicDecryptBytes(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength());
    }

    /**
     * 使用RSA算法对数据进行加解密处理，支持分段加解密。
     */
    private byte[] rsaSplitCodec(Cipher cipher, int opMode, byte[] dataBytes, int keySize) {
        int maxBlock = 0;
        if (opMode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (dataBytes.length > offSet) {
                if (dataBytes.length - offSet > maxBlock) {
                    buff = cipher.doFinal(dataBytes, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(dataBytes, offSet, dataBytes.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("crypt data quota [" + maxBlock + "] error", e);
        }
        byte[] resultDataBytes = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDataBytes;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException {
        RsaStringKeyPair keyPair = Rsa.createKeys(512);
        String plainText = "哈哈哈哈哈收拾收拾";
        Rsa rsa = new Rsa(keyPair);
        String privateEncryptText = rsa.privateEncrypt(plainText);
        String publicDecryptText = rsa.publicDecrypt(privateEncryptText);
        String publicEncryptText = rsa.publicEncrypt(plainText);
        String privateDecryptText = rsa.privateDecrypt(publicEncryptText);
        log.info("{}", keyPair);
    }
}
