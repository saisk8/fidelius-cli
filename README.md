# Fidelius CLI

Fidelius CLI is an opinionated ECDH cryptography CLI tool (based on Curve25519, corresponding key-pair generation spec, custom HMAC-based Key Derivation Function for generating AES-GCM data encryption/decryption keys).

The core logic for Fidelius CLI was excerpted from [this project](https://github.com/sukreet/fidelius). As mentioned there, the name Fidelius comes from [Fidelius Charm](https://harrypotter.fandom.com/wiki/Fidelius_Charm), a magic spell used to conceal secrets.

While Fidelius CLI can be used to serve a general purpose end-to-end encryption need, it has primarily been designed for encrypting/decrypting health data in the ABDM ecosystem (Ayushman Bharat Digital Mission — Indian Government's venture at creating a digital backbone to support the integrated digital health infrastructure of the country).

As such, apart from the code in this project, [this link](https://sandbox.abdm.gov.in/docs/data_encrypt_decrypt) can be referred for an abstract overview of the key material generation, encryption, and decryption processes.

## Usage

-   The following command(s) would build and generate .zip, and .tar distributions inside ./build/distributions

```
# BASH shell
./gradlew clean build jar

# BASH shell in Windows (e.g. GIT Bash)
TERM=cygwin ./gradlew clean build jar

# Windows CMD shell
gradlew clean build jar
```

## Usage

-   Alternatively. a pre-built release can be downloaded from here. Please ensure that JRE 1.8+ is installed, to run the binaries in the release.

-   Fidelius CLI works with the following commands

    -   generate-key-material (or gkm)
        The generate-key-material command generates ECDH key pair, and a random nonce.
    -   encrypt (or e)
        The encrypt command takes the following additional arguments:
        <string-to-encrypt> <sender-nonce> <requester-nonce> <sender-private-key> <requester-public-key>
    -   decrypt (or d)
        The decrypt command takes the following additional arguments:
        <encrypted-data> <requester-nonce> <sender-nonce> <requester-private-key> <sender-public-key>

-   The following commands exemplify the usage of the above commands.

## Key Material Generation

```
$ cd fidelius-1.0.0/bin

$ ./fidelius gkm
# OUTPUT:
{
	"privateKey": "DMxHPri8d7IT23KgLk281zZenMfVHSdeamq0RhwlIBk=",
	"publicKey": "BAheD5rUqTy4V5xR4/6HWmYpopu5CO+KO8BECS0udNqUTSNo91TIqIIy1A4Vh+F94c+n9vAcwXU2bGcfsI5f69Y=",
	"x509PublicKey": "MIIBMTCB6gYHKoZIzj0CATCB3gIBATArBgcqhkjOPQEBAiB/////////////////////////////////////////7TBEBCAqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqYSRShRAQge0Je0Je0Je0Je0Je0Je0Je0Je0Je0Je0JgtenHcQyGQEQQQqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq0kWiCuGaG4oIa04B7dLHdI0UySPU1+bXxhsinpxaJ+ztPZAiAQAAAAAAAAAAAAAAAAAAAAFN753qL3nNZYEmMaXPXT7QIBCANCAAQIXg+a1Kk8uFecUeP+h1pmKaKbuQjvijvARAktLnTalE0jaPdUyKiCMtQOFYfhfeHPp/bwHMF1NmxnH7COX+vW",
	"nonce": "6uj1RdDUbcpI3lVMZvijkMC8Te20O4Bcyz0SyivX8Eg="
}
# Let's suppose the above output represents the generated key material of the requester

$ ./fidelius gkm
# OUTPUT:
{
	"privateKey": "AYhVZpbVeX4KS5Qm/W0+9Ye2q3rnVVGmqRICmseWni4=",
	"publicKey": "BABVt+mpRLMXiQpIfEq6bj8hlXsdtXIxLsspmMgLNI1SR5mHgDVbjHO2A+U4QlMddGzqyEidzm1AkhtSxSO2Ahg=",
	"x509PublicKey": "MIIBMTCB6gYHKoZIzj0CATCB3gIBATArBgcqhkjOPQEBAiB/////////////////////////////////////////7TBEBCAqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqYSRShRAQge0Je0Je0Je0Je0Je0Je0Je0Je0Je0Je0JgtenHcQyGQEQQQqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq0kWiCuGaG4oIa04B7dLHdI0UySPU1+bXxhsinpxaJ+ztPZAiAQAAAAAAAAAAAAAAAAAAAAFN753qL3nNZYEmMaXPXT7QIBCANCAAQAVbfpqUSzF4kKSHxKum4/IZV7HbVyMS7LKZjICzSNUkeZh4A1W4xztgPlOEJTHXRs6shInc5tQJIbUsUjtgIY",
	"nonce": "lmXgblZwotx+DfBgKJF0lZXtAXgBEYr5khh79Zytr2Y="
}
# Let's suppose the above output represents the generated key material of the sender
```

## Encryption

```
# Note that the e (encrypt) command also accepts <requester-public-key> (the last argument) in X.509 standard
$ ./fidelius e "Wormtail should never have been Potter cottage's secret keeper." lmXgblZwotx+DfBgKJF0lZXtAXgBEYr5khh79Zytr2Y= 6uj1RdDUbcpI3lVMZvijkMC8Te20O4Bcyz0SyivX8Eg= AYhVZpbVeX4KS5Qm/W0+9Ye2q3rnVVGmqRICmseWni4= BAheD5rUqTy4V5xR4/6HWmYpopu5CO+KO8BECS0udNqUTSNo91TIqIIy1A4Vh+F94c+n9vAcwXU2bGcfsI5f69Y=
# OUTPUT:
{
	"encryptedData": "pzMvVZNNVtJzqPkkxcCbBUWgDEBy/mBXIeT2dJWI16ZAQnnXUb9lI+S4k8XK6mgZSKKSRIHkcNvJpllnBg548wUgavBa0vCRRwdL6kY6Yw=="
}
```

## Decryption

```
# Note that the d (decrypt) command also accepts <sender-public-key> (the last argument) in X.509 standard
$ ./fidelius d pzMvVZNNVtJzqPkkxcCbBUWgDEBy/mBXIeT2dJWI16ZAQnnXUb9lI+S4k8XK6mgZSKKSRIHkcNvJpllnBg548wUgavBa0vCRRwdL6kY6Yw== 6uj1RdDUbcpI3lVMZvijkMC8Te20O4Bcyz0SyivX8Eg= lmXgblZwotx+DfBgKJF0lZXtAXgBEYr5khh79Zytr2Y= DMxHPri8d7IT23KgLk281zZenMfVHSdeamq0RhwlIBk= BABVt+mpRLMXiQpIfEq6bj8hlXsdtXIxLsspmMgLNI1SR5mHgDVbjHO2A+U4QlMddGzqyEidzm1AkhtSxSO2Ahg=
# OUTPUT:
{
	"decryptedData": "Wormtail should never have been Potter cottage's secret keeper."
}
```
