# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Changed
- Update Selenium to version 4.34.0.

## [0.30.0] - 2025-06-10
### Changed
- Update Selenium to version 4.33.0.

### Fixed
- Send RETURN key if submit fails for input elements not in a form.

## [0.29.0] - 2025-05-19
### Changed
- Change `ZestClientElementScrollTo` to scroll with `nearest` vertical alignment, to ensure the element is kept visible.

## [0.28.0] - 2025-05-15
### Changed
- Change `ZestClientElementScrollTo` to only scroll to the element when not already in view.

## [0.27.0] - 2025-05-09
### Changed
- Change `ZestClientElementClick`, `ZestClientElementSendKeys`, and `ZestClientElementSubmit` to wait for the element to also be enabled when using `waitForMsec`.
- Update Selenium to version 4.32.0.
- Change `ZestClientElementClick` to click on the position of the element instead of the element itself when obscured, to better reproduce a manual click.

## [0.26.0] - 2025-04-25
### Added
- Allow to access the `WebElement` referenced by a `ZestClientElement`.
- New waitForMsec parameter to all client elements.

### Changed
 - Update Selenium to version 4.31.0.

## [0.25.0] - 2025-03-24
### Changed
 - Update Selenium to version 4.30.0.

## [0.24.0] - 2025-02-26
### Changed
 - Update Selenium to version 4.29.0.
 - Remove workaround that was now causing exceptions.

## [0.23.0] - 2025-01-23
### Changed
 - Update Selenium to version 4.28.0.
 - Update minimum Java version to 17.

## [0.22.0] - 2024-06-28
### Added
- Allow Zest runtimes to provide engines for script execution.

### Changed
- Update Selenium to version 4.22.0.

## [0.21.0] - 2024-04-30
### Changed
- Update Selenium to version 4.20.0.
- Update HtmlUnit to major version 3.

## [0.20.0] - 2023-10-12
### Changed
- Update Selenium to version 4.14.0.

## [0.19.0] - 2023-09-13
### Added
- Support for Zest scripts written in YAML.

### Changed
- Update code to build without deprecation warnings in newer Java versions, this might
cause invalid URLs to cause errors where previously they would be silently accepted.
- Update Selenium to version 4.12.1.

## [0.18.0] - 2023-06-26
### Added
- Client statements for Scroll, MouseOver, and Window Resize events. [#243](https://github.com/zaproxy/zest/issues/243)

### Changed
- Update Selenium to version 4.10.0.

## [0.17.0] - 2023-03-31
### Changed
- Update Selenium to version 4.

### Removed
- jBrowserDriver (JBD), Opera, and PhantomJS are no longer supported (no longer being actively maintained).

## [0.16.0] - 2022-10-07
### Changed
- Warn on running old Zest scripts from the command line rather than exit.
- Update minimum Java version to 11.

### Fixed
- Search script engines also by extension not just by name when invoking scripts otherwise it could miss some engines (e.g. Jython).

## [0.15.0] - 2020-11-24
### Changed
- Update source code and other resources per relocation of the repository, now under the ZAP
GitHub organisation. The Java classes are now under `org.zaproxy` as well as the artifact. The engine
name remains the same for compatibility with previous versions.
- Reduce the changes done to HTTP request header to the essential (e.g. authentication, HTTP state). [#214](https://github.com/zaproxy/zest/pull/214)

### Fixed
- Do not follow redirects when disabled. [#216](https://github.com/zaproxy/zest/pull/216)

## [0.14.2] - 2020-01-24
### Fixed
- Trust all certs when ignoring certificate checks, not just self signed. [#212](https://github.com/zaproxy/zest/pull/212)

## [0.14.1] - 2020-01-15
### Fixed
- Restore proxying capability, in the previous version the proxy settings were ignored. [#209](https://github.com/zaproxy/zest/pull/209)

## [0.14.0] - 2019-11-07
### Fixed
- Fix request deep copy, to use correct cookies. [#118](https://github.com/zaproxy/zest/pull/118)
- Fix Firefox proxy settings. [#121](https://github.com/zaproxy/zest/pull/121)
- Fail to open URL if no window handle found. [#128](https://github.com/zaproxy/zest/pull/128)
- Fix loop's statements copy. [#131](https://github.com/zaproxy/zest/pull/131)
- Add missing inverse state copy in expressions. [#133](https://github.com/zaproxy/zest/pull/133)
- Ignore empty URL when launching the browser. [#147](https://github.com/zaproxy/zest/pull/147)
- Include request body with PUT method. [#148](https://github.com/zaproxy/zest/pull/148)
- Fix client launch deep copy. [#150](https://github.com/zaproxy/zest/pull/150)

### Added
- Add an expression to check the protocol. [#26](https://github.com/zaproxy/zest/issues/26)
- Add assignment from DOM elements and attributes. [#117](https://github.com/zaproxy/zest/pull/117)
- Allow to start browsers, Chrome and Firefox, in headless mode and make it the default. [#122](https://github.com/zaproxy/zest/pull/122)
- Allow to use JBrowserDriver (JBD) in client launch. [#127](https://github.com/zaproxy/zest/pull/127)
- Allow to specify profile when launching Chrome and Firefox. [#152](https://github.com/zaproxy/zest/pull/152)
- Allow to obtain a screenshot from the browsers (e.g Chrome, Firefox). [#155](https://github.com/zaproxy/zest/pull/155)
- Add timestamp (unix time) to request, to indicate when it was sent. [#181](https://github.com/zaproxy/zest/pull/181)
- Command line arguments to set connection timeout and skip SSL certificate validations.
- Add statements to set/remove and assign from global variables, provided by the runtime. [#194](https://github.com/zaproxy/zest/pull/194)

### Changed
- Update PhantomJS driver to 1.4.3. [#120](https://github.com/zaproxy/zest/pull/120)
- Be more lenient when checking script type. [#126](https://github.com/zaproxy/zest/pull/126)
- Change action invoke to allow to set the charset. [#134](https://github.com/zaproxy/zest/pull/134)
- Accept insecure certs when launching browsers. [#136](https://github.com/zaproxy/zest/pull/136)
- Update HTTP client implementation, use HttpComponents Client instead of Commons HttpClient. [#168](https://github.com/zaproxy/zest/issues/168)
- Update Selenium to 3.141.59. [#169](https://github.com/zaproxy/zest/issues/169)
- Keep proxying localhost with Chrome 72+ and Firefox 67+. [#197](https://github.com/zaproxy/zest/pull/197)
- Use ISO 8601 format to de/serialise dates from/to JSON, to not depend on the system the script is running. [#202](https://github.com/zaproxy/zest/pull/202)
- Expose the variables (name/value map) in the `ZestRunner`. [#119](https://github.com/zaproxy/zest/pull/119)

### Removed
- Remove dependency on Commons HttpClient, the client implementation, and related methods. [#136](https://github.com/zaproxy/zest/pull/136)

## [0.13] - 2017-08-11
## [0.12] - 2015-01-19
## [0.11] - 2015-01-15
## [0.10] - 2015-01-02
## [0.9] - 2014-08-29
## [0.8] - 2014-06-20
## [0.7] - 2014-06-11
## [0.6] - 2014-05-16
## [0.5] - 2014-04-09
## [0.4] - 2013-09-27
## [0.3] - 2013-09-10
## [0.2] - 2013-07-18

[Unreleased]: https://github.com/zaproxy/zest/compare/0.30.0...HEAD
[0.30.0]: https://github.com/zaproxy/zest/compare/0.29.0...0.30.0
[0.29.0]: https://github.com/zaproxy/zest/compare/0.28.0...0.29.0
[0.28.0]: https://github.com/zaproxy/zest/compare/0.27.0...0.28.0
[0.27.0]: https://github.com/zaproxy/zest/compare/0.26.0...0.27.0
[0.26.0]: https://github.com/zaproxy/zest/compare/0.25.0...0.26.0
[0.25.0]: https://github.com/zaproxy/zest/compare/0.24.0...0.25.0
[0.24.0]: https://github.com/zaproxy/zest/compare/0.23.0...0.24.0
[0.23.0]: https://github.com/zaproxy/zest/compare/0.22.0...0.23.0
[0.22.0]: https://github.com/zaproxy/zest/compare/0.21.0...0.22.0
[0.21.0]: https://github.com/zaproxy/zest/compare/0.20.0...0.21.0
[0.20.0]: https://github.com/zaproxy/zest/compare/0.19.0...0.20.0
[0.19.0]: https://github.com/zaproxy/zest/compare/0.18.0...0.19.0
[0.18.0]: https://github.com/zaproxy/zest/compare/0.17.0...0.18.0
[0.17.0]: https://github.com/zaproxy/zest/compare/0.16.0...0.17.0
[0.16.0]: https://github.com/zaproxy/zest/compare/0.15.0...0.16.0
[0.15.0]: https://github.com/zaproxy/zest/compare/0.14.2...0.15.0
[0.14.2]: https://github.com/zaproxy/zest/compare/0.14.1...0.14.2
[0.14.1]: https://github.com/zaproxy/zest/compare/0.14.0...0.14.1
[0.14.0]: https://github.com/zaproxy/zest/compare/0.13...0.14.0
[0.13]: https://github.com/zaproxy/zest/compare/0.12...0.13
[0.12]: https://github.com/zaproxy/zest/compare/f4bd8e08ee0cee361e933e701bca4116d875b820...0.12
[0.11]: https://github.com/zaproxy/zest/compare/7628fe2380bc50b4eb3f44558c57b02f5677524b...f4bd8e08ee0cee361e933e701bca4116d875b820
[0.10]: https://github.com/zaproxy/zest/compare/6b0aa7ef1f986a227890d0d07ce6d59a4212ea7c...7628fe2380bc50b4eb3f44558c57b02f5677524b
[0.9]: https://github.com/zaproxy/zest/compare/f91d0eee5a05799d130bad22a2d43467f9288f9f...6b0aa7ef1f986a227890d0d07ce6d59a4212ea7c
[0.8]: https://github.com/zaproxy/zest/compare/c55a14a73dc016a14c5f2ddf2c3bac479e562def...f91d0eee5a05799d130bad22a2d43467f9288f9f
[0.7]: https://github.com/zaproxy/zest/compare/b2dd16564b0dafdc9c77053645cf9975a015fd95...c55a14a73dc016a14c5f2ddf2c3bac479e562def
[0.6]: https://github.com/zaproxy/zest/compare/b7f7240eb6e3bff6741760e4f6a434c213419eaf...b2dd16564b0dafdc9c77053645cf9975a015fd95
[0.5]: https://github.com/zaproxy/zest/compare/3533bd08bd2ff9c4f1231fa5ccebe8be567bc385...b7f7240eb6e3bff6741760e4f6a434c213419eaf
[0.4]: https://github.com/zaproxy/zest/compare/b77b59ed2938a57a1d432bfb95f567254a93a8b7...3533bd08bd2ff9c4f1231fa5ccebe8be567bc385
[0.3]: https://github.com/zaproxy/zest/compare/c2018d7485964f037819a1ee7979f64cd3c54ec5...b77b59ed2938a57a1d432bfb95f567254a93a8b7
[0.2]: https://github.com/zaproxy/zest/compare/67934d37db8147676d9455b8493628a23504ead6...c2018d7485964f037819a1ee7979f64cd3c54ec5
