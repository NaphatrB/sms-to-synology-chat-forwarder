# SMS to Synology Chat Forwarder - Project Roadmap

## Current Status (v1.0)

The application currently supports:
- ✅ Automatic SMS forwarding to Synology Chat via webhook
- ✅ Background foreground service for reliable operation
- ✅ Battery optimization exemption handling
- ✅ SMS reading and notification permissions management
- ✅ Test webhook functionality
- ✅ Boot persistence (auto-start on reboot)
- ✅ Message status tracking and history
- ✅ Automatic retry for failed messages using WorkManager
- ✅ Room database for message persistence
- ✅ Material 3 UI with Jetpack Compose
- ✅ CI/CD pipeline with GitHub Actions

---

## Short-Term Goals (v1.1 - v1.3)

### v1.1 - Enhanced Message Filtering
**Priority: High** | **Timeline: 1-2 weeks**

- [ ] **Sender Filtering**
  - Whitelist: Only forward messages from specific contacts/numbers
  - Blacklist: Ignore messages from specific contacts/numbers
  - Pattern matching for number filtering (e.g., all numbers starting with specific prefix)

- [ ] **Content Filtering**
  - Keyword-based filtering (forward only if message contains specific words)
  - Regex pattern matching for advanced filtering
  - Filter by message length

- [ ] **Filter Management UI**
  - Add/remove filters in settings
  - Enable/disable filters without deleting them
  - Import/export filter configurations

### v1.2 - Multiple Webhook Support
**Priority: High** | **Timeline: 2-3 weeks**

- [ ] **Multiple Webhook Destinations**
  - Support forwarding to multiple Synology Chat channels
  - Different webhooks for different contacts/groups
  - Priority-based webhook routing (primary, fallback)

- [ ] **Webhook Profiles**
  - Named webhook configurations
  - Quick switching between profiles
  - Profile-based filtering rules

### v1.3 - Enhanced Notifications
**Priority: Medium** | **Timeline: 1-2 weeks**

- [ ] **Notification Improvements**
  - Configurable notification sound/vibration
  - Custom notification for forwarding success/failure
  - Summary notifications (batch updates)
  - Quick actions in notifications (retry, disable temporarily)

- [ ] **Delivery Status Indicators**
  - Real-time delivery status in UI
  - Push notifications on delivery failure
  - Detailed error messages for troubleshooting

---

## Medium-Term Goals (v1.4 - v1.6)

### v1.4 - Message Templates and Formatting
**Priority: Medium** | **Timeline: 2-3 weeks**

- [ ] **Custom Message Templates**
  - Customizable message format sent to Synology Chat
  - Variable placeholders (sender name, number, timestamp, message)
  - Support for rich text formatting (bold, italic, links)
  - Template preview

- [ ] **Message Enrichment**
  - Include contact name from address book
  - Attach location if message contains address
  - Link detection and preview
  - Emoji support in templates

### v1.5 - Statistics and Analytics
**Priority: Medium** | **Timeline: 2-3 weeks**

- [ ] **Usage Statistics**
  - Total messages forwarded
  - Success/failure rate over time
  - Most active senders
  - Daily/weekly/monthly statistics

- [ ] **Analytics Dashboard**
  - Visual charts and graphs
  - Export statistics to CSV
  - Filter statistics by date range
  - Network usage tracking

- [ ] **Message Search and History**
  - Search through forwarded messages
  - Advanced filtering in history
  - Message preview
  - Export message history

### v1.6 - Smart Forwarding
**Priority: Medium** | **Timeline: 3-4 weeks**

- [ ] **Time-Based Rules**
  - Schedule forwarding (e.g., only during work hours)
  - Do Not Disturb mode with custom schedules
  - Different rules for weekdays/weekends

- [ ] **Context-Aware Forwarding**
  - Location-based rules (forward only when at specific location)
  - Device state rules (charging, battery level, WiFi/mobile data)
  - App usage context (forward differently based on active app)

- [ ] **Rate Limiting**
  - Limit number of forwards per hour/day
  - Prevent spam from specific senders
  - Batch forwarding for high-volume senders

---

## Long-Term Goals (v2.0+)

### v2.0 - Multi-Platform Support
**Priority: Low** | **Timeline: 2-3 months**

- [ ] **Expanded Platform Support**
  - Support for other chat platforms (Slack, Discord, Telegram, Teams)
  - Generic webhook support (any REST API endpoint)
  - Email forwarding option
  - MQTT integration for IoT scenarios

- [ ] **Platform-Specific Features**
  - Platform-specific message formatting
  - Support for platform-specific features (attachments, reactions)
  - Multi-platform simultaneous forwarding

### v2.1 - MMS and Media Support
**Priority: Medium** | **Timeline: 1-2 months**

- [ ] **MMS Handling**
  - Forward MMS messages with attachments
  - Image compression before forwarding
  - Support for video and audio attachments
  - Attachment size limits and warnings

- [ ] **Media Processing**
  - Automatic image upload to cloud storage
  - OCR for image-based messages
  - Audio transcription
  - Link to media files in forwarded messages

### v2.2 - Two-Way Communication
**Priority: Low** | **Timeline: 2-3 months**

- [ ] **Send SMS from Synology Chat**
  - Bot commands in Synology Chat to send SMS
  - Reply to forwarded messages
  - Send to new numbers
  - Authentication and security for outbound messages

- [ ] **Interactive Features**
  - Quick reply templates
  - Scheduled SMS sending
  - Group messaging
  - Message drafts

### v2.3 - Advanced Automation
**Priority: Low** | **Timeline: 1-2 months**

- [ ] **Automation and Scripting**
  - Custom automation rules using simple scripting
  - Integration with Tasker/Automate
  - IFTTT integration
  - REST API for external control

- [ ] **AI-Powered Features**
  - Smart message categorization
  - Spam detection using ML
  - Priority detection (urgent vs. regular)
  - Suggested replies

### v2.4 - Enterprise Features
**Priority: Low** | **Timeline: 2-3 months**

- [ ] **Multi-Device Support**
  - Sync settings across multiple devices
  - Cloud backup of configurations
  - Central management console
  - Device groups

- [ ] **Enhanced Security**
  - End-to-end encryption for forwarded messages
  - Message expiry (auto-delete after X days)
  - Secure storage with encryption at rest
  - Audit logs for compliance

- [ ] **Team/Organization Support**
  - Multiple user accounts
  - Role-based access control
  - Shared webhook configurations
  - Team-wide statistics and reporting

---

## Technical Improvements

### Code Quality and Testing
**Ongoing**

- [ ] Increase unit test coverage (target: 80%+)
- [ ] Add integration tests for webhook communication
- [ ] UI/instrumentation tests for critical flows
- [ ] Add Detekt for Kotlin code quality
- [ ] Implement KDoc documentation for all public APIs
- [ ] Set up SonarQube or similar for code analysis

### Performance Optimization
**Ongoing**

- [ ] Optimize database queries with indexes
- [ ] Implement proper caching strategies
- [ ] Reduce memory footprint
- [ ] Battery usage optimization
- [ ] Network request optimization (retry logic, timeouts)
- [ ] Background task optimization

### Architecture Improvements
**v1.x series**

- [ ] Migrate to MVVM architecture with ViewModel
- [ ] Implement proper dependency injection (Hilt/Koin)
- [ ] Use Repository pattern for data layer
- [ ] Implement proper error handling and logging
- [ ] Add crash reporting (Firebase Crashlytics or similar)
- [ ] Modularize the app for better separation of concerns

### DevOps and CI/CD
**Ongoing**

- [ ] Add automated testing in CI pipeline
- [ ] Implement code coverage reporting
- [ ] Add pre-commit hooks for code formatting
- [ ] Set up automated dependency updates (Renovate/Dependabot)
- [ ] Beta testing distribution (Firebase App Distribution)
- [ ] Automated release notes generation

---

## Documentation Improvements

### User Documentation
**Ongoing**

- [ ] Add video tutorials
- [ ] Create FAQ section
- [ ] Device-specific setup guides (Samsung, Xiaomi, etc.)
- [ ] Troubleshooting flowcharts
- [ ] Multi-language support for documentation

### Developer Documentation
**v1.x series**

- [ ] Architecture decision records (ADRs)
- [ ] API documentation
- [ ] Contributing guidelines
- [ ] Code of conduct
- [ ] Development environment setup guide
- [ ] Testing strategy documentation

---

## Community and Marketing

### Community Building
**Ongoing**

- [ ] Set up GitHub Discussions
- [ ] Create Discord/Telegram community
- [ ] Regular blog posts about development
- [ ] User showcase and testimonials
- [ ] Contributor recognition program

### Marketing
**v1.x series**

- [ ] Create demo videos
- [ ] Submit to F-Droid
- [ ] Submit to Google Play Store
- [ ] Reddit/HackerNews launch posts
- [ ] Product Hunt launch
- [ ] Create project website

---

## Research and Exploration

### Future Exploration Areas

- [ ] **Cross-Platform Development**
  - Research Kotlin Multiplatform for iOS support
  - Investigate Flutter/React Native as alternatives

- [ ] **Advanced Integrations**
  - Home automation integration (Home Assistant, OpenHAB)
  - VoIP integration
  - Calendar integration for context-aware forwarding

- [ ] **Accessibility**
  - Screen reader support
  - Voice control
  - Large text support
  - High contrast themes

- [ ] **Localization**
  - Multi-language UI support
  - RTL language support
  - Region-specific features

---

## How to Contribute

We welcome contributions! Here's how you can help:

1. **Pick an item from the roadmap** - Comment on the related issue or create one
2. **Discuss your approach** - Get feedback before starting major work
3. **Submit a PR** - Follow our contribution guidelines
4. **Help with testing** - Beta test new features and report bugs
5. **Improve documentation** - Fix typos, add examples, create tutorials

For questions or suggestions about the roadmap, please [open a discussion](../../discussions).

---

## Roadmap Updates

This roadmap is a living document and will be updated based on:
- User feedback and feature requests
- Technical feasibility assessments
- Community contributions
- Market trends and competing solutions
- Security and privacy requirements

**Last Updated:** November 2025
**Next Review:** December 2025
