/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  useColorScheme,
  Button,
  Image,
} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import CalendarModule from './CalendarModule';
import TranscoderModule from './TranscoderModule';
import {launchImageLibrary} from 'react-native-image-picker';

// import ImagePickerModule from './ImagePickerModule';

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';
  const [thumbnail, setThumbnail] = React.useState('');

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const onPress = async () => {
    try {
      const response = await launchImageLibrary({
        mediaType: 'video',
        selectionLimit: 1,
      });
      if (response.assets) {
        const path = response.assets[0].uri;
        console.log('selected video: ', path);
        if (path) {
          await TranscoderModule.compressVideo(
            "compressVideo",
            path,
            6,
            false,
            0,
            24,
            (json: string | null) => console.log(json)
          );
          console.log("helllll")
          // TranscoderModule.getFileThumbnail(
          //   "getFileThumbnail",
          //   path,
          //   1,
          //   (thumbnailPath: string) => {
          //     console.log('transcoded thumbnail: ', thumbnailPath);
          //     setThumbnail(`file://${thumbnailPath}`);
          //   },
          // );
        }
      }
    } catch (e) {
      console.log(e);
    }
  };

  // const onPress = async () => {
  //   try{
  //     // const eventId = await CalendarModule.createCalendarEvent("Hello", "World")
  //     const eventId = await ImagePickerModule.pickImage()
  //     console.log(eventId)
  //   }catch(e){
  //     console.log(e)
  //   }
  // }

  return (
    <SafeAreaView style={styles.mainViewStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        contentContainerStyle={styles.mainViewStyle}>
        <Button
          title="Click to invoke your native module!"
          color="#841584"
          onPress={onPress}
        />
        {thumbnail && (
          <Image
            style={{height: 300, width: '100%'}}
            source={{uri: thumbnail}}
          />
        )}
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
  mainViewStyle: {
    display: 'flex',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default App;
