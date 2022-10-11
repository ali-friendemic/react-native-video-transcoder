#import <React/RCTBridgeModule.h>
#import <Foundation/Foundation.h>

@interface RCT_EXTERN_MODULE(TranscoderModule, NSObject)

RCT_EXTERN_METHOD(getByteThumbnail: (NSString *)name path:(NSString *)path quality:(nonnull NSNumber *)quality result:(RCTResponseSenderBlock)callback)

RCT_EXTERN_METHOD(getFileThumbnail: (NSString *)name path:(NSString *)path quality:(nonnull NSNumber *)quality result:(RCTResponseSenderBlock)callback)

RCT_EXTERN_METHOD(getFileThumbnail: (NSString *)name path:(NSString *)path quality:(nonnull NSNumber *)quality result:(RCTResponseSenderBlock)callback)

RCT_EXTERN_METHOD(cancelCompression: (NSString *)name result:(RCTResponseSenderBlock)callback)

RCT_EXTERN_METHOD(compressVideo: (NSString *)name path:(NSString *)path quality:(nonnull NSNumber *)quality deleteOrigin:(BOOL)deleteOrigin startTime:(nonnull NSNumber *)startTime  frameRate:(nonnull NSNumber *)frameRate result:(RCTResponseSenderBlock)callback)

@end
