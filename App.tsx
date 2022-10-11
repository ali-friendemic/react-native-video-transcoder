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
  ActivityIndicator,
  StatusBar,
  StyleSheet,
  useColorScheme,
  Button,
  Image,
  Text,
  View,
} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';
// import CalendarModule from './CalendarModule';
import TranscoderModule from './TranscoderModule';
import {launchImageLibrary} from 'react-native-image-picker';
import Video from 'react-native-video';
import DropDownPicker from 'react-native-dropdown-picker';

// import ImagePickerModule from './ImagePickerModule';

interface IDetailItem {
  label: string;
  text: string;
}

const RESOLUTION = {
  DefaultQuality: 0,
  LowQuality: 1,
  MediumQuality: 2,
  HighestQuality: 3,
  Res640x480Quality: 4,
  Res960x540Quality: 5,
  Res1280x720Quality: 6,
  Res1920x1080Quality: 7,
};

const DetailItem = (props: IDetailItem) => {
  const {label, text} = props;
  return (
    <View style={styles.detailItemView}>
      <Text>{label}</Text>
      <Text>{text}</Text>
    </View>
  );
};

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';
  const [thumbnail, setThumbnail] = React.useState('');
  const [video, setVideo] = React.useState('');
  const [w, setW] = React.useState<any>(0);
  const [h, setH] = React.useState<any>(0);
  const [videoSize, setVideoSize] = React.useState<Number | null>(null);
  const [compressedSize, setCompressedSize] = React.useState<Number | null>(
    null,
  );
  const [height, setHeight] = React.useState<Number>(0);
  const [width, setWidth] = React.useState<Number>(0);

  const [open, setOpen] = React.useState(false);
  const [value, setValue] = React.useState(`${RESOLUTION.DefaultQuality}`);
  const [items, setItems] = React.useState([
    {label: 'DefaultQuality', value: '0'},
    {label: 'LowQuality', value: '1'},
    {label: 'MediumQuality', value: '2'},
    {label: 'HighestQuality', value: '3'},
    {label: 'Res640x480Quality', value: '4'},
    {label: 'Res960x540Quality', value: '5'},
    {label: 'Res1280x720Quality', value: '6'},
    {label: 'Res1920x1080Quality', value: '7'},
  ]);
  const [videoPath, setVideoPath] = React.useState('');
  const [loading, setLoading] = React.useState(false);

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
        const size = response.assets[0].fileSize;

        if (size) {
          const toKB = size / 1000;
          const toMB = toKB / 1000;
          console.log(toMB);
          setVideoSize(toMB);
          setW(response.assets[0].width);
          setH(response.assets[0].height);
        }

        console.log('selected video: ', path);
        if (path) {
          setVideoPath(path);
        }
      }
    } catch (e) {
      console.log(e);
    }
  };

  const transcodeVideo = async () => {
    try {
      setCompressedSize(null)
      setVideo('')
      setLoading(true);
      TranscoderModule.getFileThumbnail(
        'getFileThumbnail',
        videoPath,
        1,
        (thumbnailPath: string) => {
          console.log('transcoded thumbnail: ', thumbnailPath);
          setThumbnail(`file://${thumbnailPath}`);
        },
      );

      TranscoderModule.compressVideo(
        'compressVideo',
        videoPath,
        parseInt(value),
        false,
        true,
        24,
        (json: string | null) => {
          setLoading(false);
          if (json) {
            const data = JSON.parse(json);
            console.log(data);
            if (data) {
              setVideo(data.path);
              setHeight(data.height);
              setWidth(data.width);
              const toKB = data.fileSize / 1000;
              const toMB = toKB / 1000;
              setCompressedSize(toMB);
            }
          }
        },
      );
    } catch (e) {
      setLoading(false);
      console.log(e);
    }
  };

  // const onPress = async () => {
  //   try{
  //     const eventId = await CalendarModule.createCalendarEvent("Hello", "World")
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
      <View style={styles.mainViewStyle}>
        <Button
          title="Select a video to compress"
          color="#841584"
          onPress={onPress}
        />
        {videoPath && (
          <View style={{width: '75%'}}>
            <Text style={{paddingVertical: 20}}>Select quality</Text>
            <Button
              title="Start Compression"
              color="#841584"
              onPress={transcodeVideo}
            />
            <DropDownPicker
              style={{marginTop: 10}}
              listItemContainerStyle={{backgroundColor: '#841584'}}
              listItemLabelStyle={{color: 'white'}}
              open={open}
              value={value}
              items={items}
              setOpen={setOpen}
              setValue={setValue}
              setItems={setItems}
            />
          </View>
        )}

        {videoSize && (
          <>
            <DetailItem
              label="Actual Size: "
              text={`${videoSize?.toFixed(1).toString()} MB`}
            />
            <DetailItem label="Actual Dimension: " text={`${h}x${w}`} />
          </>
        )}
      </View>

      <View style={{width: '100%', alignSelf:'center'}}>
        {thumbnail && (
          <>
            <Text style={{marginVertical: 10, textAlign: 'center'}}>Thumbnail</Text>
            <Image
              style={{height: 150, width: '100%', resizeMode: 'contain'}}
              source={{uri: thumbnail}}
            />
          </>
        )}

        {compressedSize && (
          <View style={{paddingHorizontal: 50}}>
            <DetailItem
              label="Compressed Size: "
              text={`${compressedSize?.toFixed(1).toString()} MB`}
            />
            <DetailItem
              label="Compressed Dimension: "
              text={`${height}x${width}`}
            />
          </View>
        )}

        {video && (
          <View style={{paddingHorizontal: 50}}>
            <Video
              controls={true}
              source={{uri: video}}
              style={styles.backgroundVideo}
            />
          </View>
        )}

        {loading && (
          <View style={styles.loadingView}>
            <ActivityIndicator size="large" color="black" />
            <Text style={{textAlign: 'center'}}>{'Compressing...'}</Text>
          </View>
        )}
      </View>
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
    alignItems: 'center',
    justifyContent: 'center',
    paddingTop: 10,
  },
  detailItemView: {
    width: '100%',
    flexDirection: 'row',
    alignSelf: 'flex-start',
    marginTop: 10,
  },
  backgroundVideo: {
    height: 200,
    width: '100%',
  },
  loadingView: {
    width: '100%',
    height: 200,
    alignSelf: 'center',
    justifyContent: 'center',
  },
});

export default App;
