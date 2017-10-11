#import <Cordova/CDVPlugin.h>
#import "PaymentsSDK.h"

@interface PaytmPlugin : CDVPlugin<PGTransactionDelegate> {
}

// The hooks for our plugin commands
- (void)payWithPaytm:(CDVInvokedUrlCommand *)command;


@end
